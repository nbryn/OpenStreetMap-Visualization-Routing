package bfst20.presentation;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Model {

    

    public void load(File file) throws IOException, XMLStreamException, FactoryConfigurationError {
        long time = -System.nanoTime();
        String filename = file.getName();
        String fileExt = filename.substring(filename.lastIndexOf("."));
        switch (fileExt) {
            case ".bin":

                break;
            case ".txt":

                break;
            case ".osm":

                break;

            case ".zip":
                loadZip(file);
                break;
        }
        time += System.nanoTime();
        System.out.printf("Load time: %.3fms\n", time / 1e6);

    }

    private void loadZip(File file) {
        try {
            ZipFile zipFile = new ZipFile(file.toString());

            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();

                if(name.endsWith(".osm")){
                    InputStream stream = zipFile.getInputStream(entry);
                    System.out.println(new String(stream.readAllBytes()));
                }
            }

            zipFile.close();


        } catch (IOException ex) {
            System.out.println(ex);
        }

    }


}
