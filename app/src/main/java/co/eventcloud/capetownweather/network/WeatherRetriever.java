package co.eventcloud.capetownweather.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import co.eventcloud.capetownweather.BuildConfig;
import co.eventcloud.capetownweather.weather.model.DailyWeatherInfo;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Contains helper methods related to retrieving the weather from the API
 *
 * <p/>
 * Created by Laurie on 2017/08/10.
 */

public class WeatherRetriever {

    public static final String BASE_URL = BuildConfig.API_URL;

    /**
     * Asynchronously does an API request to retrieve the weather
     * @see co.eventcloud.capetownweather.network.WeatherApi#getWeather(String, String, String, String, Integer, Float)
     */
    public static void getWeather(@NonNull String latitude,
                                  @NonNull String longitude,
                                  @Nullable String units,
                                  @Nullable Integer delay,
                                  @Nullable Float chaos,
                                  @NonNull Callback<DailyWeatherInfo> callback) {

        // Create a logging interceptor (to show detailed logs about the request and response).
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        // Create the retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create the API object
        WeatherApi weatherApi = retrofit.create(WeatherApi.class);

        // Create the API call
        Call<DailyWeatherInfo> call = weatherApi.getWeather(BuildConfig.API_KEY, latitude, longitude, units, delay, chaos);

        // Make the call asynchronously and notify the callback of the response (which might be a success or error)
        call.enqueue(callback);
    }
}
