package bfst20.logic;

import bfst20.logic.entities.Bounds;
import bfst20.logic.entities.LinePath;

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

    private AppController appController;
    private static boolean isLoaded = false;
    private static FileHandler fileHandler;

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
                System.out.println("hej");
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


        try {
            FileOutputStream fileOut = new FileOutputStream(file, false);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(drawables);
            objectOut.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadBinary(File file) throws FileNotFoundException {
        try (var in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            try {

                Map<Type, List<LinePath>> drawables = (Map<Type, List<LinePath>>) in.readObject();
                Bounds bounds = drawables.get(Type.BOUNDS).get(0).getBounds();

                appController.setBoundsOnModel(bounds);
                appController.setLinePathsOnModel(drawables);
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
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
                    Parser parser = Parser.getInstance();
                    parser.parseString(new String(stream.readAllBytes()));
                }
            }

            zipFile.close();


        } catch (IOException | XMLStreamException ex) {
            System.out.println(ex);
        }

    }

}
