package co.eventcloud.capetownweather.weather.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thbs.skycons.library.SkyconView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import co.eventcloud.capetownweather.R;
import co.eventcloud.capetownweather.realm.dao.WeatherDao;
import co.eventcloud.capetownweather.realm.model.RealmCurrentWeatherInfo;
import co.eventcloud.capetownweather.utils.IconUtil;
import co.eventcloud.capetownweather.weather.event.WeatherInfoUpdatedEvent;
import io.realm.Realm;

/**
 * Fragment showing the current weather
 * <p>
 * <p/>
 * Created by Laurie on 2017/08/11.
 */

public class CurrentWeatherFragment extends Fragment implements WeatherView {

    @BindView(R.id.summary)
    TextView summary;
    @BindView(R.id.real_temperature)
    TextView realTemperature;
    @BindView(R.id.skycon_placeholder)
    FrameLayout skyconPlaceholder;
    @BindView(R.id.apparent_temperature)
    TextView apparentTemperature;
    @BindView(R.id.precipitation)
    TextView precipitation;
    @BindView(R.id.humidity)
    TextView humidity;
    @BindView(R.id.wind_speed)
    TextView windSpeed;
    Unbinder unbinder;

    RealmCurrentWeatherInfo currentWeatherInfo;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.humidity_wind_container)
    LinearLayout humidityWindContainer;

    private Realm realm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm = Realm.getDefaultInstance();

        currentWeatherInfo = WeatherDao.getCurrentWeatherInfo(realm);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_weather, container, false);

        unbinder = ButterKnife.bind(this, view);

        realm = Realm.getDefaultInstance();

        setInfo();

        return view;
    }

    private void setInfo() {
        if (currentWeatherInfo != null) {
            int timeInMillis = currentWeatherInfo.getTime() * 1000;

            DateTimeZone dateTimeZone = DateTimeZone.forID("Africa/Johannesburg");

            Instant instant = new Instant(timeInMillis);

            DateTime dateTime = instant.toDateTime(dateTimeZone);

            DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");

            time.setText(String.format(getString(R.string.time), dateTime.toString(fmt)));

            summary.setText(currentWeatherInfo.getSummary());

            String temperature = String.format(getString(R.string.temperature), currentWeatherInfo.getTemperature().intValue());
            realTemperature.setText(temperature);

            String apparentTemperatureString = String.format(getString(R.string.apparentTemperature), currentWeatherInfo.getApparentTemperature().intValue());
            apparentTemperature.setText(apparentTemperatureString);

            String humidityString = String.format(getString(R.string.humidity), (int)(currentWeatherInfo.getHumidity() * 100));
            humidity.setText(humidityString);

            String precipitationString = String.format(getString(R.string.precipitation), (int)(currentWeatherInfo.getPrecipitationProbability() * 100));
            precipitation.setText(precipitationString);

            String windSpeedString = String.format(getString(R.string.windSpeed), (int)(currentWeatherInfo.getWindSpeed() * 3.6));
            windSpeed.setText(windSpeedString);

            String iconString = currentWeatherInfo.getIcon();

            SkyconView skyconView = IconUtil.getSkyconView(getContext(), iconString);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            skyconView.setLayoutParams(params);

            skyconPlaceholder.addView(skyconView);

            if (!realm.isClosed()) {
                realm.close();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(WeatherInfoUpdatedEvent event) {
        realm = Realm.getDefaultInstance();

        currentWeatherInfo = WeatherDao.getCurrentWeatherInfo(realm);
        setInfo();

        EventBus.getDefault().removeStickyEvent(event);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
