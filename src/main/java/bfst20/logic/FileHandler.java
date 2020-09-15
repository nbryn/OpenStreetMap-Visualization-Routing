package bfst20.logic;

import bfst20.logic.controllers.interfaces.AddressAPI;
import bfst20.logic.controllers.interfaces.KDTreeAPI;
import bfst20.logic.controllers.interfaces.LinePathAPI;
import bfst20.logic.controllers.interfaces.OSMElementAPI;
import bfst20.logic.entities.Bounds;
import bfst20.logic.entities.LinePath;
import javafx.scene.control.Alert;
import bfst20.logic.misc.OSMType;
import bfst20.logic.routing.Graph;
import bfst20.logic.ternary.TST;
import bfst20.logic.kdtree.*;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileHandler {
    private Parser parser;
    private OSMElementAPI osmElementAPI;
    private KDTreeAPI kdTreeAPI;
    private AddressAPI addressAPI;
    private LinePathAPI linePathAPI;

    private FileHandler() {

    }

    public static class Builder {
        private Parser parser;
        private OSMElementAPI osmElementAPI;
        private KDTreeAPI kdTreeAPI;
        private AddressAPI addressAPI;
        private LinePathAPI linePathAPI;

        public Builder(Parser parser) {
            this.parser = parser;

        }

        public Builder withKDTreeAPI(KDTreeAPI kdTreeAPI) {
            this.kdTreeAPI = kdTreeAPI;

            return this;
        }

        public Builder withAddressAPI(AddressAPI addressAPI) {
            this.addressAPI = addressAPI;

            return this;
        }

        public Builder withLinePathAPI(LinePathAPI linePathAPI) {
            this.linePathAPI = linePathAPI;

            return this;
        }

        public Builder withOSMElementAPI(OSMElementAPI osmElementAPI) {
            this.osmElementAPI = osmElementAPI;

            return this;
        }

        public FileHandler build() {
            FileHandler fileHandler = new FileHandler();
            fileHandler.parser = this.parser;
            fileHandler.osmElementAPI = this.osmElementAPI;
            fileHandler.linePathAPI = this.linePathAPI;
            fileHandler.kdTreeAPI = this.kdTreeAPI;
            fileHandler.addressAPI = this.addressAPI;


            return fileHandler;

        }

    }

    public void load(File file, AppController appController) throws IOException, XMLStreamException, FactoryConfigurationError {
        try {
            String filename = file.getName();
            String fileExt = filename.substring(filename.lastIndexOf("."));
            switch (fileExt) {
                case ".bin":
                    loadBinary(file, appController);
                    break;
                case ".osm":
                    parser.parseOSMFile(file);
                    break;
                case ".zip":
                    loadZip(file, appController);
                    break;
            }
        } catch (OutOfMemoryError e) {
            appController.alertOK(Alert.AlertType.ERROR, "Error loading, out of memory, exiting.", true);
            System.exit(1);
        }
    }

    private void loadBinary(File file, AppController appController) {
        try (var in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            Bounds bounds = (Bounds) in.readObject();
            Map<OSMType, KDTree> trees = (Map<OSMType, KDTree>) in.readObject();
            List<LinePath> coastline = (List<LinePath>) in.readObject();
            TST tst = (TST) in.readObject();
            Graph graph = (Graph) in.readObject();

            osmElementAPI.saveBoundsData(bounds);
            kdTreeAPI.saveAllKDTrees(trees);
            linePathAPI.saveCoastlines(coastline);
            addressAPI.saveTSTData(tst);
            appController.saveGraphData(graph);
        } catch (IOException e) {
            appController.alertOK(Alert.AlertType.ERROR, "Error loading the binary file, exiting.", true);
            System.exit(1);
        } catch (ClassNotFoundException e) {
            appController.alertOK(Alert.AlertType.ERROR, "Invalid binary file, exiting.", true);
            System.exit(1);
        }
    }

    private void loadZip(File file, AppController appController) {
        try {
            ZipFile zipFile = new ZipFile(file.toString());
            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();

                if (name.endsWith(".osm")) {
                    InputStream stream = zipFile.getInputStream(entry);
                    String toBeParsed = new String(stream.readAllBytes());
                    appController.parseString(toBeParsed);
                }
            }

            zipFile.close();

        } catch (IOException | XMLStreamException ex) {
            appController.alertOK(Alert.AlertType.ERROR, "Error loading zip file, exiting.", true);
            System.exit(1);
        }
    }

    public void generateBinary() throws IOException {
        File file = new File("samsoe.bin");
        file.createNewFile();
        writeToFile(file);
    }

    private void writeToFile(File file) throws IOException {
        AppController appController = new AppController();
        FileOutputStream fileOut = new FileOutputStream(file, false);
        ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);

        objectOut.writeObject(osmElementAPI.fetchBoundsData());
        objectOut.writeObject(kdTreeAPI.fetchAllKDTrees());
        objectOut.writeObject(linePathAPI.fetchCoastlines());

        objectOut.writeObject(addressAPI.fetchTSTData());
        objectOut.writeObject(appController.fetchGraphData());
        objectOut.close();
    }

    public static File getResourceAsFile(String resourcePath, AppController appController) {
        try {
            String suffix = resourcePath.endsWith(".osm") ? ".osm" : ".bin";

            InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(resourcePath);
            if (in == null) return null;

            File tempFile = File.createTempFile(String.valueOf(in.hashCode()), suffix);
            tempFile.deleteOnExit();

            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                //copy stream
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            return tempFile;
        } catch (IOException e) {
            appController.alertOK(Alert.AlertType.ERROR, "Error loading file stream, exiting.", true);
            System.exit(1);
            return null;
        }
    }
}
