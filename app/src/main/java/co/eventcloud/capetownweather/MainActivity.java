package co.eventcloud.capetownweather;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.eventcloud.capetownweather.weather.CurrentWeatherFragment;
import co.eventcloud.capetownweather.weather.DailyWeatherFragment;
import co.eventcloud.capetownweather.weather.HourlyWeatherFragment;
import co.eventcloud.capetownweather.weather.WeatherBroadcastReceiver;

/**
 * The main activity of the app. This activity has 3 tabLayout, showing the following:
 * <p>
 * <ol>
 * <li>Current Forecast (what the weather is like now at this moment)</li>
 * <li>Hourly Forecast</li>
 * <li>Daily Forecast</li>
 * </ol>
 * <p>
 * Created by Laurie on 2017/08/10.
 */
@SuppressWarnings({"CanBeFinal", "unused"})
public class MainActivity extends AppCompatActivity {

    /**
     * The toolbar showing the title of the app
     */
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    /**
     * The tab layout containing three tabs - Now, Hourly, and Daily forecasts
     */
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        WeatherBroadcastReceiver weatherBroadcastReceiver = new WeatherBroadcastReceiver();
        weatherBroadcastReceiver.setAlarm(this);

        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // return the appropriate fragment
            switch (position) {
                case 0:
                    return new CurrentWeatherFragment();
                case 1:
                    return new HourlyWeatherFragment();
                case 2:
                    return new DailyWeatherFragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.now);
                case 1:
                    return getString(R.string.hourly);
                case 2:
                    return getString(R.string.daily);
            }
            return null;
        }
    }
}
