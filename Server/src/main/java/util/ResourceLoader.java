package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URL;

/**
 * Library class which is used to obtain application resources. Base path is se to resources directory.
 *
 * @author Oldřich Hradil
 * @version 1.0
 */
public class ResourceLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceLoader.class);

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
        return ResourceLoader.class.getClassLoader().getSystemResource(path);
    }

    /**
     * @param path Relative path which root is in resources folder.
     * @return Input stream of resource
     */
    public static InputStream getResourceAsInputStream(String path) {
        System.out.println(ClassLoader.getSystemResourceAsStream(path).toString());
        return ClassLoader.getSystemResourceAsStream(path);
    }


}
