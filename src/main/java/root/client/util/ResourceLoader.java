package root.client.util;

import org.apache.commons.io.FileSystemUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
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
     *
     * @param path Relative path which root is in resources folder.
     * @return File representation of the resource
     */
    public static File getResourceAsFile(String path) {
        return new File(ClassLoader.getSystemResource(path).toString().replace("file:/", ""));
    }

    public static List<String> getSingleplayerMaps() {
        File[] mapFiles = getResourceAsFile("plans/singleplayer").listFiles();
        List<String> mapNames = new LinkedList<>();
        Arrays.stream(mapFiles).forEach(mapFile -> {
            mapNames.add(FilenameUtils.removeExtension(mapFile.getName()));
        });
        return mapNames;
    }
    public static List<String> getMultiplayerMaps() {
        File[] mapFiles = getResourceAsFile("plans/multiplayer").listFiles();
        List<String> mapNames = new LinkedList<>();
        Arrays.stream(mapFiles).forEach(mapFile -> {
            mapNames.add(FilenameUtils.removeExtension(mapFile.getName()));
        });
        return mapNames;
    }
}
