package co.eventcloud.capetownweather.weather.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The weather overview info POJO. This contains the current weather information, hourlyWeatherInfo weather updates as well as dailyWeatherInfo updates.
 *
 * <p/>
 * Created by Laurie on 2017/08/10.
 */

public class WeatherInfo {

    @SerializedName("currentWeatherInfo")
    @Expose
    private CurrentWeatherInfo currentWeatherInfo;
    @SerializedName("hourlyWeatherInfo")
    @Expose
    private HourlyWeatherInfo hourlyWeatherInfo;
    @SerializedName("dailyWeatherInfo")
    @Expose
    private DailyWeatherInfo dailyWeatherInfo;

    public CurrentWeatherInfo getCurrentWeatherInfo() {
        return currentWeatherInfo;
    }

    public void setCurrentWeatherInfo(CurrentWeatherInfo currently) {
        this.currentWeatherInfo = currently;
    }

    public HourlyWeatherInfo getHourlyWeatherInfo() {
        return hourlyWeatherInfo;
    }

    public void setHourlyWeatherInfo(HourlyWeatherInfo hourly) {
        this.hourlyWeatherInfo = hourly;
    }

    public DailyWeatherInfo getDailyWeatherInfo() {
        return dailyWeatherInfo;
    }

    public void setDailyWeatherInfo(DailyWeatherInfo daily) {
        this.dailyWeatherInfo = daily;
    }
}
