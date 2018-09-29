package root.client.controller;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import root.client.model.map.Box;
import root.client.model.map.MapPart;
import root.client.model.map.Position;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MapTypeAdapter extends TypeAdapter<List<List<MapPart>>> {

    @Override
    public void write(JsonWriter jsonWriter, List<List<MapPart>> mapParts) throws IOException {
        jsonWriter.beginArray();
        for (int row = 0; row < mapParts.size(); row++) {
            jsonWriter.beginObject().name(String.valueOf(row)).beginArray();
            for (int column = 0; column < mapParts.get(row).size(); column++) {
                MapPart mapPart = mapParts.get(row).get(column);
                jsonWriter.beginObject().
                        name("className").value(mapPart.getClass().getName()).
                        name("position").beginObject().name("row").value(mapPart.getPosition().row).name("column").value(mapPart.getPosition().column).endObject().
                        endObject();
            }
            jsonWriter.endArray().endObject();
        }
        jsonWriter.endArray();

    }

    @Override
    public List<List<MapPart>> read(JsonReader jsonReader) throws IOException {

        List<List<MapPart>> mapParts = new LinkedList<>();

        jsonReader.beginArray();;
        while (jsonReader.hasNext()) {
            try {
                jsonReader.beginObject();
                jsonReader.nextName();
                jsonReader.beginArray();
                List<MapPart> row = new LinkedList<>();
                while (jsonReader.hasNext()) {
                    jsonReader.beginObject();
                    jsonReader.nextName();
                    String name = jsonReader.nextString();
                    jsonReader.nextName();
                    jsonReader.beginObject();
                    jsonReader.nextName();
                    int rowNum = jsonReader.nextInt();
                    jsonReader.nextName();
                    int columnNum = jsonReader.nextInt();
                    Position position = new Position(rowNum, columnNum);
                  //  System.out.println(name);
                    jsonReader.endObject();
                    jsonReader.endObject();
                    Class<?> clazz = null;
                    clazz = Class.forName(name);
                    Constructor<?> ctor = clazz.getConstructor(Position.class);
                    row.add((MapPart) ctor.newInstance(position));

                }
                jsonReader.endArray();
                jsonReader.endObject();
                mapParts.add(row);
            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException | IllegalStateException e) {
               // e.printStackTrace();
                break;

            }

        }
        jsonReader.endArray();
        return mapParts;
    }
}
