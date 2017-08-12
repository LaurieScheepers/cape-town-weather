package co.eventcloud.capetownweather.weather.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Object describing the Week's Weather Info we get back from the API. This object contains info about each day's weather
 *
 * <p/>
 * Created by Laurie on 2017/08/10.
 */

@SuppressWarnings("unused")
public class WeekWeatherInfo {

    @SerializedName("summary")
    @Expose
    private String summary;

    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("data")
    @Expose
    private List<DayInfo> data = null;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<DayInfo> getData() {
        return data;
    }

    public void setData(List<DayInfo> data) {
        this.data = data;
    }
}
