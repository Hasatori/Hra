package root.client.model;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class FileLoader {


    private FileLoader() {

    }

    public static URL loadFileURL(String path) {
        return ClassLoader.getSystemResource(path);
    }

    public static InputStream loadFileInputStream(String path) {
        return ClassLoader.getSystemResourceAsStream(path);
    }

    public static File loadFile(String path) {
        return new File(ClassLoader.getSystemResource(path).toString().replace("file:/", ""));
    }
}
