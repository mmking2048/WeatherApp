package vandy.mooc.model.aidl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import vandy.mooc.model.aidl.WeatherData.Main;
import vandy.mooc.model.aidl.WeatherData.Sys;
import vandy.mooc.model.aidl.WeatherData.Weather;
import vandy.mooc.model.aidl.WeatherData.Wind;
import android.util.JsonReader;
import android.util.JsonToken;

/**
 * Parses the Json weather data returned from the Weather Services API
 * and returns a List of WeatherData objects that contain this data.
 */
public class WeatherDataJsonParser {
    /**
     * Used for logging purposes.
     */
    private final String TAG =
        this.getClass().getCanonicalName();

    /**
     * Parse the @a inputStream and convert it into a List of JsonWeather
     * objects.
     */

    public List<WeatherData> parseJsonStream(InputStream inputStream)
            throws IOException {

        // TODO -- you fill in here.

        JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));

        try {
            return parseJsonWeatherDataArray(reader);
        } finally {
            reader.close();
        }
    }

    /**
     * Parse a Json stream and convert it into a List of WeatherData
     * objects.
     */
    public List<WeatherData> parseJsonWeatherDataArray(JsonReader reader)
            throws IOException {
        // TODO -- you fill in here.

        // Check if we get a valid response by checking the cod.


        List<WeatherData> weatherDatas = new ArrayList<>();

        weatherDatas.add(parseJsonWeatherData(reader));

        return weatherDatas;

    }

    /**
     * Parse a Json stream and return a WeatherData object.
     */
    public WeatherData parseJsonWeatherData(JsonReader reader)
            throws IOException {

        // TODO -- you fill in here.
        String name = null;
        long date = 0;
        long cod = 0;
        Sys sys = null;
        Main main = null;
        Wind wind = null;
        List<Weather> weathers = null;
        String message = null;

        reader.beginObject();

        while (reader.hasNext()) {
            String tagName = reader.nextName();

            switch (tagName) {

                case "name":
                    name = reader.nextString();
                    break;

                case "date":
                    date = reader.nextLong();
                    break;

                case "cod":
                    cod = reader.nextLong();
                    break;

                case "sys":
                    sys = parseSys(reader);
                    break;

                case "main":
                    main = parseMain(reader);
                    break;

                case "wind":
                    wind = parseWind(reader);
                    break;

                case "weather":
                    weathers = parseWeathers(reader);
                    break;

                case "message":
                    message = reader.nextString();
                    break;

                default:
                    reader.skipValue();
                    break;
            }
        }

        reader.endObject();

        WeatherData weatherData = new WeatherData(name, date, cod, sys, main, wind, weathers);
        weatherData.setMessage(message);
        return weatherData;
    }

    /**
     * Parse a Json stream and return a List of Weather objects.
     */
    public List<Weather> parseWeathers(JsonReader reader) throws IOException {
        // TODO -- you fill in here.

        List<Weather> weathers = new ArrayList<>();

        reader.beginArray();

        while (reader.hasNext()) {
            weathers.add(parseWeather(reader));
        }

        reader.endArray();

        return weathers;
    }

    /**
     * Parse a Json stream and return a Weather object.
     */
    public Weather parseWeather(JsonReader reader) throws IOException {
        // TODO -- you fill in here.

        long id = 0;
        String main = null;
        String description = null;
        String icon = null;

        reader.beginObject();

        while (reader.hasNext()) {
            String tagName = reader.nextName();

            switch (tagName) {

                case "id":
                    id = reader.nextLong();
                    break;

                case "main":
                    main = reader.nextString();
                    break;

                case "description":
                    description = reader.nextString();
                    break;

                case "icon":
                    icon = reader.nextString();
                    break;

                default:
                    reader.skipValue();
                    break;
            }
        }

        reader.endObject();

        Weather weather = new Weather(id, main, description, icon);
        return weather;
    }

    /**
     * Parse a Json stream and return a Main Object.
     */
    public Main parseMain(JsonReader reader)
            throws IOException {
        // TODO -- you fill in here.

        double temp = 0;
        long humidity = 0;
        double pressure = 0;

        reader.beginObject();

        while (reader.hasNext()) {
            String tagName = reader.nextName();

            switch (tagName) {

                case "temp":
                    temp = reader.nextDouble();
                    break;

                case "humidity":
                    humidity = reader.nextLong();
                    break;

                case "pressure":
                    pressure = reader.nextDouble();
                    break;

                default:
                    reader.skipValue();
                    break;
            }
        }

        reader.endObject();

        Main main = new Main(temp, humidity, pressure);
        return main;
    }

    /**
     * Parse a Json stream and return a Wind Object.
     */
    public Wind parseWind(JsonReader reader) throws IOException {
        // TODO -- you fill in here.

        double speed = 0;
        double deg = 0;

        reader.beginObject();

        while (reader.hasNext()) {
            String tagName = reader.nextName();

            switch (tagName) {

                case "speed":
                    speed = reader.nextDouble();
                    break;

                case "deg":
                    deg = reader.nextDouble();
                    break;

                default:
                    reader.skipValue();
                    break;
            }
        }

        reader.endObject();

        Wind wind = new Wind(speed, deg);
        return wind;
    }

    /**
     * Parse a Json stream and return a Sys Object.
     */
    public Sys parseSys(JsonReader reader)
            throws IOException {
        // TODO -- you fill in here.

        long sunrise = 0;
        long sunset = 0;
        String country = null;
        double message = 0;

        reader.beginObject();

        while (reader.hasNext()) {
            String tagName = reader.nextName();

            switch (tagName) {

                case "sunset":
                    sunset = reader.nextLong();
                    break;

                case "sunrise":
                    sunrise = reader.nextLong();
                    break;

                case "country":
                    country = reader.nextString();
                    break;

                case "message":
                    message = reader.nextDouble();
                    break;

                default:
                    reader.skipValue();
                    break;
            }
        }

        reader.endObject();

        Sys sys = new Sys(sunrise, sunset, country);
        sys.setMessage(message);
        return sys;
    }
}
