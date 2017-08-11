package co.eventcloud.capetownweather.weather;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;

import timber.log.Timber;

/**
 * A broadcast receiver that receives an intent each time the "alarm" (every 30 minutes) goes off.
 * This class starts the background weather service that retrieves the weather from the API.
 *
 * <p/>
 * Created by Laurie on 2017/08/10.
 */

public class WeatherBroadcastReceiver extends WakefulBroadcastReceiver {

    /**
     * The alarm manager responsible for setting the alarm
     */
    private AlarmManager alarmManager;

    /**
     * The intent that must be delivered each time the alarm goes off
     */
    private PendingIntent alarmIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.d("Alarm has gone off, start the service");
        Intent weatherService = new Intent(context, WeatherService.class);
        startWakefulService(context, weatherService);
    }

    public void setAlarm(Context context) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, WeatherBroadcastReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Timber.d("Setting the alarm");

        // Configure the alarm manager to send the alarm intent every 15 minutes. Note, using inexact repeats are better for the device's battery.
        // Also, this deviates a bit from the specs in that the intent is sent every 15 mins instead of 20.
        // This is because for inexact repeats we must use one of the defined constants (see https://developer.android.com/training/scheduling/alarms.html for best practises)
        //alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, alarmIntent);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, 1000 * 10, alarmIntent);

        // Enable the alarm
        ComponentName receiver = new ComponentName(context, WeatherBroadcastReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void cancelAlarm() {
        if (alarmManager != null) {
            alarmManager.cancel(alarmIntent);
        }
    }
}
