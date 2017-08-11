package co.eventcloud.capetownweather.weather.event;

/**
 * Event meant to notify the UI that an error occurred when trying to get the weather from the API
 *
 * <p/>
 * Created by Laurie on 2017/08/12.
 */

public class WeatherInfoUpdateErrorEvent {
    public WeatherInfoUpdateErrorEvent() {
        // Nothing needed in constructor, the event is only meant to notify the UI - no extra information required
    }
}
