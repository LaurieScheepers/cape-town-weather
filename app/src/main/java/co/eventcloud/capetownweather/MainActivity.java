package co.eventcloud.capetownweather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import co.eventcloud.capetownweather.weather.WeatherBroadcastReceiver;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
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
