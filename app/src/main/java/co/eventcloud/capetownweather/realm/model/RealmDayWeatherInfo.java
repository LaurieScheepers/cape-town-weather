package co.eventcloud.capetownweather.realm.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Realm object describing the day's weather, containing each hour's info object
 *
 * <p/>
 * Created by Laurie on 2017/08/10.
 */

public class RealmDayWeatherInfo extends RealmObject {

    public static final String FIELD_ID = "id";

    @PrimaryKey
    private int id;

    private String summary;

    private String icon;

    private RealmList<RealmHourInfo> data = null;

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

    public RealmList<RealmHourInfo> getData() {
        return data;
    }

    public void setData(RealmList<RealmHourInfo> data) {
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
