package co.eventcloud.capetownweather.weather.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The weather overview info POJO. This contains the current weather information, dayWeatherInfo weather updates as well as weekWeatherInfo updates.
 *
 * <p/>
 * Created by Laurie on 2017/08/10.
 */

public class WeatherInfo {

    @SerializedName("currentWeatherInfo")
    @Expose
    private CurrentWeatherInfo currentWeatherInfo;
    @SerializedName("dayWeatherInfo")
    @Expose
    private DayWeatherInfo dayWeatherInfo;
    @SerializedName("weekWeatherInfo")
    @Expose
    private WeekWeatherInfo weekWeatherInfo;

    public CurrentWeatherInfo getCurrentWeatherInfo() {
        return currentWeatherInfo;
    }

    public void setCurrentWeatherInfo(CurrentWeatherInfo currently) {
        this.currentWeatherInfo = currently;
    }

    public DayWeatherInfo getDayWeatherInfo() {
        return dayWeatherInfo;
    }

    public void setDayWeatherInfo(DayWeatherInfo hourly) {
        this.dayWeatherInfo = hourly;
    }

    public WeekWeatherInfo getWeekWeatherInfo() {
        return weekWeatherInfo;
    }

    public void setWeekWeatherInfo(WeekWeatherInfo daily) {
        this.weekWeatherInfo = daily;
    }
}
