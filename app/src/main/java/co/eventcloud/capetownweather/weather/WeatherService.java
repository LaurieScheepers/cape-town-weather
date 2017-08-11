package co.eventcloud.capetownweather.weather;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import co.eventcloud.capetownweather.network.WeatherRetriever;
import co.eventcloud.capetownweather.weather.callback.WeatherUpdateListener;
import timber.log.Timber;

/**
 * The service that will handle the API calls done in the background
 *
 * <p/>
 *
 * Created by Laurie on 2017/08/10.
 */

public class WeatherService extends IntentService {

    public WeatherService() {
        super("WeatherService");
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        Timber.d("Getting the weather from the API");

        if (intent != null) {
            WeatherRetriever.getWeather(new WeatherUpdateListener() {
                @Override
                public void onWeatherFinishedUpdating() {
                    // Pass the intent back to release the wake lock
                    WeatherBroadcastReceiver.completeWakefulIntent(intent);
                }
            });
        }
    }
}
