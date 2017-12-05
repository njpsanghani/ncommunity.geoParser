import com.google.gson.stream.JsonReader;
import model.GeoData;
import model.GeoPoint;
import model.GeoPolygon;

import java.io.FileReader;
import java.io.IOException;

public class GeoParser {

    public static void main(String args[]) {
        try {
            GeoData geoData = parseFromFile("map.geojson");
            System.out.println(geoData.getGeoPolygons().size());
            System.out.println(geoData.getGeoPoints().size());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public enum GeoType {
        Point, MultiPoint, LineString, MultiLineString, Polygon, MultiPolygon, GeometryCollection, Feature, FeatureCollection
    }

    public static GeoData parseFromFile(String filePath) throws IOException, NullPointerException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        JsonReader jsonReader = new JsonReader(new FileReader(classloader.getResource(filePath).getFile()));
        String currentType = null;
        String keyPair = null;
        GeoType geoType = null;
        GeoData geoData = new GeoData();
        peek(jsonReader, currentType, geoType, keyPair, geoData);
        return geoData;
    }

    private static void peek(JsonReader jsonReader, String currentType, GeoType geoType, String keyPair, GeoData geoData) throws IOException {
        switch (jsonReader.peek()) {
            case NAME:
                keyPair = jsonReader.nextName();
                parseKeyPair(jsonReader, currentType, geoType, keyPair, geoData);
                break;
            case STRING:
                peek(jsonReader, currentType, geoType, keyPair, geoData);
                break;
            case NUMBER:
                peek(jsonReader, currentType, geoType, keyPair, geoData);
                break;
            case BOOLEAN:
                peek(jsonReader, currentType, geoType, keyPair, geoData);
                break;
            case NULL:
                peek(jsonReader, currentType, geoType, keyPair, geoData);
                break;
            case BEGIN_OBJECT:
                jsonReader.beginObject();
                keyPair = "";
                peek(jsonReader, currentType, geoType, keyPair, geoData);
                break;
            case END_OBJECT:
                jsonReader.endObject();
                keyPair = "";
                peek(jsonReader, currentType, geoType, keyPair, geoData);
                break;
            case BEGIN_ARRAY:
                jsonReader.beginArray();
                keyPair = "";
                peek(jsonReader, currentType, geoType, keyPair, geoData);
                break;
            case END_ARRAY:
                jsonReader.endArray();
                keyPair = "";
                peek(jsonReader, currentType, geoType, keyPair, geoData);
                break;
            case END_DOCUMENT:
                jsonReader.close();
                break;
        }
    }

    private static void parseKeyPair(JsonReader jsonReader, String currentType, GeoType geoType, String keyPair, GeoData geoData) throws IOException {
        switch (keyPair) {
            case "type":
                currentType = jsonReader.nextString();
                geoType = getGeoType(currentType);
                peek(jsonReader, currentType, geoType, keyPair, geoData);
                break;
            case "features":

                if (currentType != null && currentType.equalsIgnoreCase(GeoType.FeatureCollection.name())) {
                    geoType = GeoType.FeatureCollection;
                    peek(jsonReader, currentType, geoType, keyPair, geoData);
                }

                if (currentType != null && currentType.equalsIgnoreCase(GeoType.Feature.name())) {
                    geoType = GeoType.Feature;
                    peek(jsonReader, currentType, geoType, keyPair, geoData);
                }

                if (currentType != null && currentType.equalsIgnoreCase(GeoType.Point.name())) {
                    geoType = GeoType.Point;
                    peek(jsonReader, currentType, geoType, keyPair, geoData);
                }

                if (currentType != null && currentType.equalsIgnoreCase(GeoType.Point.name())) {
                    geoType = GeoType.Polygon;
                    peek(jsonReader, currentType, geoType, keyPair, geoData);
                }

                break;

            case "properties":
                peek(jsonReader, currentType, geoType, keyPair, geoData);
                break;

            case "feature":
                currentType = jsonReader.nextString();
                peek(jsonReader, currentType, geoType, keyPair, geoData);
                break;

            case "geometry":
                peek(jsonReader, currentType, geoType, keyPair, geoData);
                break;
            case "coordinates":
                parseCoordinates(jsonReader, currentType, geoType, keyPair, geoData);
                break;
            default:
                //peek(jsonReader, currentType, geoType, keyPair);
                break;
        }
    }

    private static GeoType getGeoType(String s) {
        switch (s) {
            case "Point":
                return GeoType.Point;
            case "MultiPoint":
                return GeoType.MultiPoint;
            case "LineString":
                return GeoType.LineString;
            case "MultiLineString":
                return GeoType.MultiLineString;
            case "Polygon":
                return GeoType.Polygon;
            case "MultiPolygon":
                return GeoType.MultiPolygon;
            case "GeometryCollection":
                return GeoType.GeometryCollection;
            case "Feature":
                return GeoType.Feature;
            case "FeatureCollection":
                return GeoType.FeatureCollection;
            default:
                return null;
        }
    }

    private static void parseCoordinates(JsonReader jsonReader, String currentType, GeoType geoType, String keyPair, GeoData geoData) throws IOException {
        switch (geoType) {
            case Point:
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    Double longitude = jsonReader.nextDouble(); // Geojson RFC format Longitude is first
                    Double latitude = jsonReader.nextDouble();
                    GeoPoint geoPoint = new GeoPoint(latitude, longitude);
                    geoData.getGeoPoints().add(geoPoint);
                }
                peek(jsonReader, currentType, geoType, keyPair, geoData);
                break;
            /*case MultiPoint:
                break;*/
            case Polygon:
                jsonReader.beginArray();
                jsonReader.beginArray();
                GeoPolygon geoPolygon = new GeoPolygon();
                while (jsonReader.hasNext()) {
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        Double longitude = jsonReader.nextDouble(); // Geojson RFC format Longitude is first
                        Double latitude = jsonReader.nextDouble();
                        GeoPoint geoPoint = new GeoPoint(latitude, longitude);
                        geoPolygon.getGeoPointList().add(geoPoint);
                    }
                    jsonReader.endArray();
                }
                geoPolygon.buildArea();
                geoData.getGeoPolygons().add(geoPolygon);
                peek(jsonReader, currentType, geoType, keyPair, geoData);
                break;
            /*case MultiPolygon:
                break;*/
            default:
                jsonReader.skipValue();
                peek(jsonReader, currentType, geoType, keyPair, geoData);
                break;

        }
    }
}
