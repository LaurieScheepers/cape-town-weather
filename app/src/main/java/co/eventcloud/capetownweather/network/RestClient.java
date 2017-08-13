package co.eventcloud.capetownweather.network;

import co.eventcloud.capetownweather.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The main class responsible for communication with the API
 *
 * <p/>
 * Created by Laurie on 2017/08/13.
 */

public class RestClient {

    private static final String BASE_URL = BuildConfig.API_URL;

    private static RestClient restClient;
    private static Retrofit retrofit;

    public static RestClient getClient() {
        if (restClient == null) {
            restClient = new RestClient();
        }

        return restClient;
    }

    private RestClient() {
        // Create a logging interceptor (to show detailed logs about the request and response).
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        // Create the retrofit instance
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public WeatherApi getWeatherApi() {
        WeatherApi weatherApi = retrofit.create(WeatherApi.class);

        return weatherApi;
    }
}
