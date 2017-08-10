package co.eventcloud.capetownweather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import co.eventcloud.capetownweather.weather.WeatherBroadcastReceiver;

/**
 * The main activity of the app. This activity has one goal: to display the weather information for Cape Town.
 *
 * <p/>
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // First start - set the wakeful broadcast receiver alarm
        WeatherBroadcastReceiver weatherBroadcastReceiver = new WeatherBroadcastReceiver();
        weatherBroadcastReceiver.setAlarm(this);
    }
}
