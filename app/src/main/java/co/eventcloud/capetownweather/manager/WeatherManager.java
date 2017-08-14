package co.eventcloud.capetownweather.manager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

import co.eventcloud.capetownweather.BuildConfig;
import co.eventcloud.capetownweather.R;
import co.eventcloud.capetownweather.network.RestClient;
import co.eventcloud.capetownweather.network.WeatherApi;
import co.eventcloud.capetownweather.realm.dao.WeatherDao;
import co.eventcloud.capetownweather.weather.callback.WeatherUpdateListener;
import co.eventcloud.capetownweather.weather.event.WeatherInfoUpdateErrorEvent;
import co.eventcloud.capetownweather.weather.event.WeatherInfoUpdatedEvent;
import co.eventcloud.capetownweather.weather.model.CurrentWeatherInfo;
import co.eventcloud.capetownweather.weather.model.WeatherInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * The Weather manager. This class is responsible for making API calls
 *
 * <p/>
 * Created by Laurie on 2017/08/10.
 */

public class WeatherManager {

    /**
     * Flag indicating that the app is busy with an API request to get the weather
     */
    public static boolean busyGettingWeatherFromApi;

    /**
     * Asynchronously does an API request to retrieve the weather
     * @see co.eventcloud.capetownweather.network.WeatherApi#getWeather(String, String, String, String, String, Integer, Float)
     */
    private static void getWeather(@SuppressWarnings("SameParameterValue") @NonNull String latitude,
                                  @SuppressWarnings("SameParameterValue") @NonNull String longitude,
                                  @SuppressWarnings("SameParameterValue") @Nullable String exclude,
                                  @SuppressWarnings("SameParameterValue") @Nullable String units,
                                  @Nullable Integer delay,
                                  @Nullable Float chaos,
                                  @NonNull Callback<WeatherInfo> callback) {

        Timber.d("Getting weather from API");

        WeatherApi weatherApi = RestClient.getClient().getWeatherApi();

        // Create the API call
        Call<WeatherInfo> call = weatherApi.getWeather(BuildConfig.API_KEY, latitude, longitude, exclude, units, delay, chaos);

        // Make the call asynchronously and notify the callback of the response (which might be a success or error)
        call.enqueue(callback);
    }

    /**
     * Helper method to do the GET weather API call.
     * @param context the Context to use
     * @param listener a WeatherUpdateListener that will fire a callback once the update is complete
     * @param delay the delay in seconds before the response is sent by the server
     * @param chaos the chaos level (probability of server sending 503)
     */
    private static void getWeather(final Context context, final WeatherUpdateListener listener, int delay, float chaos) {
        busyGettingWeatherFromApi = true;

        // Get Weather from the API
        getWeather(BuildConfig.CAPE_TOWN_LATITUDE, BuildConfig.CAPE_TOWN_LONGITUDE, "minutely", "si", delay, chaos, new Callback<WeatherInfo>() {
            @Override
            public void onResponse(@NonNull Call<WeatherInfo> call, @NonNull Response<WeatherInfo> response) {
                busyGettingWeatherFromApi = false;

                if (response.isSuccessful()) {
                    Timber.d("YAY, WE GOT THE WEATHER, MAN!");

                    WeatherInfo weatherInfo = response.body();

                    if (weatherInfo != null) {
                        CurrentWeatherInfo currentWeatherInfo = weatherInfo.getCurrentWeatherInfo();
                        int currentTemp = currentWeatherInfo.getTemperature().intValue();

                        // Save the weather information to the DB
                        WeatherDao.saveWeatherInfo(weatherInfo);

                        EventBus.getDefault().postSticky(new WeatherInfoUpdatedEvent());

                        if (listener != null) {
                            listener.onWeatherFinishedUpdating();
                        }

                        WeatherNotificationManager.sendWeatherWarningNotificationIfNecessary(context, currentTemp);
                    }
                } else {
                    int statusCode = response.code();

                    Timber.e("OH NO, something went wrong with retrieving the weather. Status Code = " + statusCode);

                    String errorMessage;

                    // Check for errors
                    if (statusCode == 503) { // SERVICE_UNAVAILABLE - might be because of unreliable network
                        errorMessage = context.getString(R.string.error_service_unavailable);
                    } else {
                        errorMessage = context.getString(R.string.error_generic);
                    }

                    if (listener != null) {
                        listener.onWeatherUpdateError(errorMessage);
                    }

                    EventBus.getDefault().postSticky(new WeatherInfoUpdateErrorEvent());
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherInfo> call, @NonNull Throwable t) {
                Timber.e(t, "OH NO, something went wrong with retrieving the weather");

                if (listener != null) {
                    listener.onWeatherUpdateError(context.getString(R.string.error_generic));
                }
            }
        });
    }

    /**
     * Helper method to do the GET weather API call.
     */
    public static void getWeather(final Context context, final WeatherUpdateListener listener) {
        getWeather(context, listener, BuildConfig.DEBUG ? 3 : 0, BuildConfig.DEBUG ? 0.5f : 0f);
    }
}
