package root.client.util;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * Library class which is used to obtain application resources. Base path is se to resources directory.
 * @author Oldřich Hradil
 * @version 1.0
 */
public class ResourceLoader {

    /**
     * Private constructor since this is a library class.
     */
    private ResourceLoader() {
    }

    /**
     * @param path Relative path which root is in resources folder.
     * @return URL to given source
     */
    public static URL gerResourceURL(String path) {
        return ClassLoader.getSystemResource(path);
    }

    /**
     * @param path Relative path which root is in resources folder.
     * @return Input stream of resource
     */
    public static InputStream getResourceAsInputStream(String path) {
        return ClassLoader.getSystemResourceAsStream(path);
    }

    /**
     * Returns file representation of the resource. It can be either file or directory.
     * @param path Relative path which root is in resources folder.
     * @return File representation of the resource
     */
    public static File getResourceAsFile(String path) {
        return new File(ClassLoader.getSystemResource(path).toString().replace("file:/", ""));
    }
}
