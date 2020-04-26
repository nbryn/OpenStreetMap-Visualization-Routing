package bfst20.logic;

import bfst20.logic.entities.Address;
import bfst20.logic.entities.Bounds;
import bfst20.logic.entities.LinePath;
import bfst20.logic.entities.Node;
import javafx.scene.control.Alert;
import bfst20.logic.misc.OSMType;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.util.ArrayList;
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

    public void generateBinary() throws IOException {
        File file = new File("samsoe.bin");
        file.createNewFile();

        Map<OSMType, List<LinePath>> linePaths = appController.getLinePathsFromModel();


        writeToFile(file, linePaths);
    }

    private void writeToFile(File file, Map<OSMType, List<LinePath>> linePaths) throws FileNotFoundException, IOException {
        FileOutputStream fileOut = new FileOutputStream(file, false);
        ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
        objectOut.writeObject(appController.getBoundsFromModel());
        objectOut.writeObject(linePaths);
        objectOut.writeObject(appController.getAddressesFromModel());
        objectOut.writeObject(appController.getHighwaysFromModel());
        objectOut.close();
    }

    public void loadBinary(File file)  {
        try (var in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            Bounds bounds = (Bounds) in.readObject();
            Map<OSMType, List<LinePath>> linePaths = (Map<OSMType, List<LinePath>>) in.readObject();
            Map<Long, Address> addresses = (Map<Long, Address>) in.readObject();
            List<LinePath> highWays = (List<LinePath>) in.readObject();

            appController.addToModel(bounds);
            appController.addToModel(linePaths);
            appController.addToModelAddresses(addresses);
            appController.addToModelHighways(highWays);
        } catch (IOException e) {
            appController.alertOK(Alert.AlertType.ERROR, "Error loading the binary file, exiting.");
            System.exit(1);
        } catch (ClassNotFoundException e){
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
                    appController.startStringParsing(toBeParsed);
                }
            }

            zipFile.close();

        } catch (IOException | XMLStreamException ex) {
            appController.alertOK(Alert.AlertType.ERROR, "Error loading zip file, exiting.");
            System.exit(1);
        }
    }
}
