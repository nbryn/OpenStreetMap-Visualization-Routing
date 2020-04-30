package bfst20.logic;

import bfst20.logic.entities.Address;
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
    private static boolean isLoaded = false;
    private static FileHandler fileHandler;
    private AppController appController;

    private FileHandler() {
        appController = new AppController();
    }

    public static FileHandler getInstance() {
        if (!isLoaded) {
            isLoaded = true;
            fileHandler = new FileHandler();
        }
        return fileHandler;
    }

    public void load(File file) throws IOException, XMLStreamException, FactoryConfigurationError {
        
        appController.clearNodeData();
        appController.clearLinePathData();

        long time = -System.nanoTime();
        String filename = file.getName();
        String fileExt = filename.substring(filename.lastIndexOf("."));
        switch (fileExt) {
            case ".bin":
                loadBinary(file);
                break;
            case ".txt":

                break;
            case ".osm":
                appController.parseOSM(file);
                break;
            case ".zip":
                loadZip(file);
                break;
        }
        time += System.nanoTime();
        System.out.printf("Load time: %.3fms\n", time / 1e6);

    }

    private void loadBinary(File file) {
        try (var in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            Bounds bounds = (Bounds) in.readObject();
            Map<OSMType, KDTree> tree = (Map<OSMType, KDTree>) in.readObject();
            List<LinePath> coastline = (List<LinePath>) in.readObject();
            TST tst = (TST) in.readObject();
            Graph graph = (Graph) in.readObject();

            appController.addToModel(bounds);
            appController.setAllKDTrees(tree);
            appController.addCoastLine(coastline);
            appController.addTst(tst);
            appController.addToModel(graph);
        } catch (IOException e) {
            e.printStackTrace();
            appController.alertOK(Alert.AlertType.ERROR, "Error loading the binary file, exiting.");
            System.exit(1);
        } catch (ClassNotFoundException e) {
            appController.alertOK(Alert.AlertType.ERROR, "Invalid binary file, exiting.");
            System.exit(1);
        }
    }

    private void loadZip(File file) {
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
            appController.alertOK(Alert.AlertType.ERROR, "Error loading zip file, exiting.");
            System.exit(1);
        }
    }

    public void generateBinary() throws IOException {
        File file = new File("samsoe.bin");
        file.createNewFile();
        writeToFile(file);
    }

    private void writeToFile(File file) throws FileNotFoundException, IOException {
        FileOutputStream fileOut = new FileOutputStream(file, false);
        ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
        objectOut.writeObject(appController.getBoundsFromModel());
        objectOut.writeObject(appController.getAllLKDTrees());
        objectOut.writeObject(appController.getCoastlines());
        objectOut.writeObject(appController.getTST());
        objectOut.writeObject(appController.getGraphFromModel());
        objectOut.close();
    }
}
