package co.eventcloud.capetownweather.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import co.eventcloud.capetownweather.weather.WeatherBroadcastReceiver;

/**
 * A broadcast receiver that receives an intent when the device has finished booting
 *
 * <p/>
 * Created by Laurie on 2017/08/10.
 */

public class BootCompletedReceiver extends BroadcastReceiver {
    WeatherBroadcastReceiver weatherBroadcastReceiver = new WeatherBroadcastReceiver();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm once the device has booted
            weatherBroadcastReceiver.setAlarm(context);
        }
    }
}
