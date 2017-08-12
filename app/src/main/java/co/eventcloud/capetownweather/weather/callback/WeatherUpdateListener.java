package co.eventcloud.capetownweather.weather.callback;

/**
 * A listener that will invoke a callback once the weather has been updated
 *
 * <p/>
 * Created by Laurie on 2017/08/11.
 */

public interface WeatherUpdateListener {

    /**
     * Callback invoked when the weather has finished updating
     */
    void onWeatherFinishedUpdating();

    /**
     * Callback invoked when there was an error in getting the weather from the API
     * @param errorMessage the error message to be displayed
     */
    void onWeatherUpdateError(String errorMessage);
}
