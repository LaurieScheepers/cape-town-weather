package co.eventcloud.capetownweather.realm.dao;

import android.support.annotation.NonNull;

import java.util.List;

import co.eventcloud.capetownweather.realm.model.RealmCurrentWeatherInfo;
import co.eventcloud.capetownweather.realm.model.RealmDayInfo;
import co.eventcloud.capetownweather.realm.model.RealmDayWeatherInfo;
import co.eventcloud.capetownweather.realm.model.RealmHourInfo;
import co.eventcloud.capetownweather.realm.model.RealmWeekWeatherInfo;
import co.eventcloud.capetownweather.weather.model.CurrentWeatherInfo;
import co.eventcloud.capetownweather.weather.model.HourInfo;
import co.eventcloud.capetownweather.weather.model.DayWeatherInfo;
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
        realmCurrentWeatherInfo.setPrecipitationProbability(currentWeatherInfo.getPrecipProbability());
        realmCurrentWeatherInfo.setSummary(currentWeatherInfo.getSummary());
        realmCurrentWeatherInfo.setTemperature(currentWeatherInfo.getTemperature());
        realmCurrentWeatherInfo.setTime(currentWeatherInfo.getTime());
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

        return realm.where(RealmCurrentWeatherInfo.class).findFirst();
    }

    public static void saveWeekWeatherInfo(@NonNull WeekWeatherInfo weekWeatherInfo) {
        Realm realm = Realm.getDefaultInstance();

        final RealmWeekWeatherInfo realmDailyWeatherInfo = new RealmWeekWeatherInfo();

        realmDailyWeatherInfo.setId(0); // Set id as 0 - there should always only be one RealmWeekWeatherInfo object in the db
        realmDailyWeatherInfo.setSummary(weekWeatherInfo.getSummary());
        realmDailyWeatherInfo.setIcon(weekWeatherInfo.getIcon());

        RealmList<RealmDayInfo> realmDayInfoList = new RealmList<>();

        List<DayInfo> dailyWeatherInfoList = weekWeatherInfo.getData();

        int id = 0;

        for (DayInfo info : dailyWeatherInfoList) {
            final RealmDayInfo realmDayInfo = new RealmDayInfo();

            realmDayInfo.setId(id);
            realmDayInfo.setSummary(info.getSummary());
            realmDayInfo.setIcon(info.getIcon());
            realmDayInfo.setWindSpeed(info.getWindSpeed());
            realmDayInfo.setTime(info.getTime());
            realmDayInfo.setTemperatureMin(info.getTemperatureMin());
            realmDayInfo.setTemperatureMax(info.getTemperatureMax());
            realmDayInfo.setApparentTemperatureMin(info.getApparentTemperatureMin());
            realmDayInfo.setApparentTemperatureMax(info.getApparentTemperatureMax());
            realmDayInfo.setHumidity(info.getHumidity());

            realmDayInfoList.add(realmDayInfo);

            id++;
        }

        realmDailyWeatherInfo.setData(realmDayInfoList);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(realmDailyWeatherInfo);
            }
        });

        realm.close();
    }

    public static RealmWeekWeatherInfo getWeekWeatherInfo(@NonNull Realm realm) {

        return realm.where(RealmWeekWeatherInfo.class).findFirst();
    }

    public static void saveDayWeatherInfo(@NonNull DayWeatherInfo dayWeatherInfo) {
        final Realm realm = Realm.getDefaultInstance();

        final RealmDayWeatherInfo realmDayWeatherInfo = new RealmDayWeatherInfo();

        realmDayWeatherInfo.setId(0); // Set id as 0 - there should always only be one RealmDayWeatherInfo object in the db
        realmDayWeatherInfo.setSummary(dayWeatherInfo.getSummary());
        realmDayWeatherInfo.setIcon(dayWeatherInfo.getIcon());

        RealmList<RealmHourInfo> realmHourInfoList = new RealmList<>();

        List<HourInfo> hourInfoList = dayWeatherInfo.getData();

        int id = 0;

        for (HourInfo info : hourInfoList) {
            final RealmHourInfo realmHourInfo = new RealmHourInfo();

            realmHourInfo.setId(id);
            realmHourInfo.setIcon(info.getIcon());
            realmHourInfo.setSummary(info.getSummary());
            realmHourInfo.setHumidity(info.getHumidity());
            realmHourInfo.setTemperature(info.getTemperature());
            realmHourInfo.setApparentTemperature(info.getApparentTemperature());
            realmHourInfo.setTime(info.getTime());
            realmHourInfo.setWindSpeed(info.getWindSpeed());

            realmHourInfoList.add(realmHourInfo);

            id++;
        }

        realmDayWeatherInfo.setData(realmHourInfoList);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(realmDayWeatherInfo);
            }
        });

        realm.close();
    }

    public static RealmDayWeatherInfo getDayWeatherInfo(@NonNull Realm realm) {
        return realm.where(RealmDayWeatherInfo.class).findFirst();
    }
}
