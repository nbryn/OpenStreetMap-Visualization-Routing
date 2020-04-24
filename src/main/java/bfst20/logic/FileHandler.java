package bfst20.logic;

import bfst20.logic.entities.Bounds;
import bfst20.logic.entities.LinePath;
import bfst20.presentation.ErrorMessenger;
import javafx.scene.control.Alert;

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

        Map<Type, List<LinePath>> drawables = appController.getLinePathsFromModel();
        Bounds bounds = appController.getBoundsFromModel();

        drawables.put(Type.BOUNDS, new ArrayList<>());
        drawables.get(Type.BOUNDS).add(new LinePath(bounds.getMaxLat(), bounds.getMaxLon(), bounds.getMinLat(), bounds.getMinLon()));

        writeToFile(file, drawables);
    }

    private void writeToFile(File file, Map<Type, List<LinePath>> drawables) throws FileNotFoundException, IOException {
        FileOutputStream fileOut = new FileOutputStream(file, false);
        ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
        objectOut.writeObject(drawables);
        objectOut.close();
    }

    public void loadBinary(File file)  {
        try (var in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            Map<Type, List<LinePath>> linePaths = (Map<Type, List<LinePath>>) in.readObject();
            Bounds bounds = linePaths.get(Type.BOUNDS).get(0).getBounds();

            appController.addToModel(bounds);
            appController.addToModel(linePaths);
        } catch (IOException e) {
            ErrorMessenger.alertOK(Alert.AlertType.ERROR, "Error loading the binary file, exiting.");
            System.exit(1);
        } catch (ClassNotFoundException e){
            ErrorMessenger.alertOK(Alert.AlertType.ERROR, "Invalid binary file, exiting.");
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
            ErrorMessenger.alertOK(Alert.AlertType.ERROR, "Error loading zip file, exiting.");
            System.exit(1);
        }
    }
}
