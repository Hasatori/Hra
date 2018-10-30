package client.util;

import client.controller.multiplayer.MultiplayerController;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.ls.LSOutput;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
        return ClassLoader.getSystemResource(path);
    }

    /**
     * @param path Relative path which root is in resources folder.
     * @return Input stream of resource
     */
    public static InputStream getResourceAsInputStream(String path) {
        return ClassLoader.getSystemResourceAsStream(path);
    }

    public static List<String> getSingleplayerMaps() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(getResourceAsInputStream("plans/singleplayer")));
        String name;
        List<String> mapNames = new LinkedList<>();
        while ((name = reader.readLine()) != null) {
            mapNames.add(FilenameUtils.removeExtension(name));
        }
        reader.close();
        return mapNames;
    }

    public static List<String> getMultiplayerMaps() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(getResourceAsInputStream("plans/multiplayer")));
        String name;
        List<String> mapNames = new LinkedList<>();
        while ((name = reader.readLine()) != null) {
            mapNames.add(FilenameUtils.removeExtension(name));
        }
        reader.close();
        return mapNames;
    }
}
