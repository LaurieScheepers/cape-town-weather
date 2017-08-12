package co.eventcloud.capetownweather.realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Realm object describing the current weather info
 *
 * <p/>
 * Created by Laurie on 2017/08/10.
 */

@SuppressWarnings("unused")
public class RealmCurrentWeatherInfo extends RealmObject {

    public static final String FIELD_ID = "id";

    @PrimaryKey
    private int id;

    private int time;

    private String summary;

    private String icon;

    private Double temperature;

    private Double apparentTemperature;

    private Double humidity;

    private Double windSpeed;

    private Double precipitationProbability;

    public int getId() {
        return id;
    }

    @SuppressWarnings("SameParameterValue")
    public void setId(int id) {
        this.id = id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

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

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getApparentTemperature() {
        return apparentTemperature;
    }

    public void setApparentTemperature(Double apparentTemperature) {
        this.apparentTemperature = apparentTemperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Double getPrecipitationProbability() {
        return precipitationProbability;
    }

    public void setPrecipitationProbability(Double precipitationProbability) {
        this.precipitationProbability = precipitationProbability;
    }
}
