package co.eventcloud.capetownweather.realm.dao;

import android.support.annotation.NonNull;

import java.util.List;

import co.eventcloud.capetownweather.realm.model.RealmCurrentWeatherInfo;
import co.eventcloud.capetownweather.realm.model.RealmDayInfo;
import co.eventcloud.capetownweather.realm.model.RealmWeekWeatherInfo;
import co.eventcloud.capetownweather.weather.model.CurrentWeatherInfo;
import co.eventcloud.capetownweather.weather.model.DayInfo;
import co.eventcloud.capetownweather.weather.model.WeekWeatherInfo;
import io.realm.Realm;
import io.realm.RealmList;

/**
 * Contains helper methods related to saving/reading Weather info from the database
 *
 * <p/>
 * Created by Laurie on 2017/08/11.
 */

public class WeatherDao {

    /**
     * Saves the current weather info object to the DB
     */
    public static void saveCurrentWeather(@NonNull CurrentWeatherInfo currentWeatherInfo) {
        Realm realm = Realm.getDefaultInstance();

        final RealmCurrentWeatherInfo realmCurrentWeatherInfo = new RealmCurrentWeatherInfo();

        realmCurrentWeatherInfo.setId(0);   // Set id as 0 - there should always only be one RealmCurrentWeatherInfo object in the db
        realmCurrentWeatherInfo.setApparentTemperature(currentWeatherInfo.getApparentTemperature());
        realmCurrentWeatherInfo.setHumidity(currentWeatherInfo.getHumidity());
        realmCurrentWeatherInfo.setIcon(currentWeatherInfo.getIcon());
        realmCurrentWeatherInfo.setSummary(currentWeatherInfo.getSummary());
        realmCurrentWeatherInfo.setTemperature(currentWeatherInfo.getTemperature());
        realmCurrentWeatherInfo.setTime((int) (currentWeatherInfo.getTime().getTime() / 1000));
        realmCurrentWeatherInfo.setWindSpeed(currentWeatherInfo.getWindSpeed());

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(realmCurrentWeatherInfo);
            }
        });

        realm.close();
    }

    /**
     * Gets the current weather info from the DB
     */
    public static RealmCurrentWeatherInfo getCurrentWeatherInfo(@NonNull Realm realm) {

        RealmCurrentWeatherInfo currentWeatherInfo = realm.where(RealmCurrentWeatherInfo.class).findFirst();

        return currentWeatherInfo;
    }

    public static void saveWeekWeatherInfo(@NonNull WeekWeatherInfo weekWeatherInfo) {
        Realm realm = Realm.getDefaultInstance();

        final RealmWeekWeatherInfo realmDailyWeatherInfo = new RealmWeekWeatherInfo();

        realmDailyWeatherInfo.setId(0); // Set id as 0 - there should always only be one RealmWeekWeatherInfo object in the db
        realmDailyWeatherInfo.setSummary(weekWeatherInfo.getSummary());
        realmDailyWeatherInfo.setIcon(weekWeatherInfo.getIcon());

        RealmList<RealmDayInfo> realmDayInfoRealmList = new RealmList<>();

        List<DayInfo> dailyWeatherInfoList = weekWeatherInfo.getData();

        int id = 0;

        for (DayInfo info : dailyWeatherInfoList) {
            RealmDayInfo realmDayInfo = new RealmDayInfo();

            realmDayInfo.setId(id);
            realmDayInfo.setSummary(info.getSummary());
            realmDayInfo.setIcon(info.getIcon());
            realmDayInfo.setWindSpeed(info.getWindSpeed());
            realmDayInfo.setTime((int) (info.getTime().getTime() / 1000));
            realmDayInfo.setTemperature(info.getTemperature());
            realmDayInfo.setApparentTemperature(info.getApparentTemperature());
            realmDayInfo.setHumidity(info.getHumidity());

            realmDayInfoRealmList.add(realmDayInfo);

            id++;
        }

        realmDailyWeatherInfo.setData(realmDayInfoRealmList);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(realmDailyWeatherInfo);
            }
        });

        realm.close();
    }

    public static RealmWeekWeatherInfo getWeekWeatherInfo(@NonNull Realm realm) {
        RealmWeekWeatherInfo realmWeekWeatherInfo = realm.where(RealmWeekWeatherInfo.class).findFirst();

        return realmWeekWeatherInfo;
    }

//    public static void saveRealmDayWeatherInfo(@NonNull DayWeatherInfo dayWeatherInfo) {
//        final Realm realm = Realm.getDefaultInstance();
//    }
}
