package co.eventcloud.capetownweather.weather;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * A converter that converts the time (seconds since in epoch) given in the API to a Java Date object
 *
 * <p/>
 * Created by root on 2017/08/10.
 */

public class DateConverter implements JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        // Get the JSON element value as a string
        String s = json.getAsJsonPrimitive().getAsString();

        // The time given by the API is in seconds
        int time = Integer.parseInt(s);

        // Construct a date object from the time (must pass in milliseconds)
        Date d = new Date(time * 1000);

        return d;
    }
}
