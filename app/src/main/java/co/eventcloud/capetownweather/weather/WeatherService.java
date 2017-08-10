package co.eventcloud.capetownweather.weather;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import co.eventcloud.capetownweather.BuildConfig;
import co.eventcloud.capetownweather.network.WeatherRetriever;
import co.eventcloud.capetownweather.weather.model.WeatherInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
    protected void onHandleIntent(@Nullable Intent intent) {
        Timber.d("Getting the weather from the API");

        // Get Weather from the API
        WeatherRetriever.getWeather(BuildConfig.CAPE_TOWN_LATITUDE, BuildConfig.CAPE_TOWN_LONGITUDE, "si", 0, 0f, new Callback<WeatherInfo>() {
            @Override
            public void onResponse(Call<WeatherInfo> call, Response<WeatherInfo> response) {
                Timber.d("YAY, WE GOT THE WEATHER, MAN!");
            }

            @Override
            public void onFailure(Call<WeatherInfo> call, Throwable t) {
                Timber.e(t, "OH NO, something went wrong with retrieving the weather");
            }
        });

        // Pass the intent back to release the wake lock
        WeatherBroadcastReceiver.completeWakefulIntent(intent);
    }
}
