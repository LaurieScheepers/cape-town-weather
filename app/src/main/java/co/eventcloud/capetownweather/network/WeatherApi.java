package co.eventcloud.capetownweather.network;

import co.eventcloud.capetownweather.weather.model.WeatherInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * The API used to get the weather in Cape Town
 *
 * <p/>
 * Created by Laurie on 2017/08/10.
 */

interface WeatherApi {

    /**
     * Gets the weather from darksky.net API, using Eventcloud's proxy.
     *
     * <p/>
     *
     * An example URL looks like the following: <br>
     *
     * http://ec-weather-proxy.appspot.com/forecast/29e4a4ce0ec0068b03fe203fa81d457f/-33.9249,18.4241?units=si&delay=5&chaos=0.2
     *
     * @param apiKey The secret API key to use for the request
     * @param latitude The latitude of the location
     * @param longitude The longitude of the location
     * @param exclude An optional parameter used to exclude some weather info
     * @param units The units in which to return the weather conditions
     * @param delay The waiting time in seconds before returning a response (to simulate slow networks)
     * @param chaos The probability of returning a 503 error response (to simulate unreliable networks)
     *
     * @return The weather model object containing all the necessary information
     */
    @GET("/forecast/{api_key}/{lat},{lng}")
    Call<WeatherInfo> getWeather(@SuppressWarnings("SameParameterValue") @Path("api_key") String apiKey,
                                 @Path("lat") String latitude,
                                 @Path("lng") String longitude,
                                 @Query("exclude") String exclude,
                                 @Query("units") String units,
                                 @Query("delay") Integer delay,
                                 @Query("chaos") Float chaos);

}
