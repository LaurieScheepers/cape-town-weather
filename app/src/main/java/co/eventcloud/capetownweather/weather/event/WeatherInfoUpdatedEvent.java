package co.eventcloud.capetownweather.weather.event;

/**
 * An event meant to notify the UI that the weather info has been updated (the alarm has gone off, and the API call has been done)
 *
 * <p/>
 * Created by Laurie on 2017/08/11.
 */

public class WeatherInfoUpdatedEvent {

    public WeatherInfoUpdatedEvent() {
        // Nothing needed in constructor, the event is only meant to notify the UI - no extra information required
    }
}
