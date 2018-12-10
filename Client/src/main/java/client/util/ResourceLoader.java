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

    /**
     * Fetches list of singleplayer maps from a file.
     * @return list of singleplayer maps
     * @throws IOException file not found error
     */
    public static List<String> getSingleplayerMaps() throws IOException {
        return  getMaps("plans/singleplayer/maps.txt");
    }

    /**
     * Fetches list of multiplayer maps from a file.
     * @return list of multiplayer maps
     * @throws IOException file not found error
     */
    public static List<String> getMultiplayerMaps() throws IOException {
        return getMaps("plans/multiplayer/maps.txt");
    }

    /**
     * Fetches all available maps.
     * @param path path to file
     * @return List of map names
     * @throws IOException file not found error
     */
    private static List<String> getMaps(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(getResourceAsInputStream(path)));
        String name;
        List<String> mapNames = new LinkedList<>();
        while ((name = reader.readLine()) != null) {
            System.out.println(name);
            mapNames.add(FilenameUtils.removeExtension(name));
        }
        reader.close();
        return mapNames;
    }
}
