package co.eventcloud.capetownweather.weather.callback;

/**
 * A listener that will invoke a callback once the weather has been updated
 *
 * <p/>
 * Created by Laurie on 2017/08/11.
 */

public interface WeatherUpdateListener {

    void onWeatherFinishedUpdating();
}
