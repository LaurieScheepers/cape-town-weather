package co.eventcloud.capetownweather.weather.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import co.eventcloud.capetownweather.R;
import co.eventcloud.capetownweather.network.WeatherRetriever;
import co.eventcloud.capetownweather.realm.dao.WeatherDao;
import co.eventcloud.capetownweather.realm.model.RealmCurrentWeatherInfo;
import co.eventcloud.capetownweather.utils.IconUtil;
import co.eventcloud.capetownweather.weather.callback.WeatherUpdateListener;
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
    @BindView(R.id.swipeToRefreshLayout)
    SwipeRefreshLayout swipeToRefreshLayout;

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

        swipeToRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorAccent));

        swipeToRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                WeatherRetriever.getWeather(new WeatherUpdateListener() {
                    @Override
                    public void onWeatherFinishedUpdating() {
                        swipeToRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });

        realm = Realm.getDefaultInstance();

        setInfo();

        return view;
    }

    private void setInfo() {
        if (currentWeatherInfo != null) {
            if (swipeToRefreshLayout.isRefreshing()) {
                swipeToRefreshLayout.setRefreshing(false);
            }

            long timeInMillis = currentWeatherInfo.getTime() * 1000L;

            Date date = new Date(timeInMillis);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Africa/Johannesburg"));

            time.setText(String.format(getString(R.string.time), simpleDateFormat.format(date)));

            summary.setText(currentWeatherInfo.getSummary());

            String temperature = String.format(getString(R.string.temperature), currentWeatherInfo.getTemperature().intValue());
            realTemperature.setText(temperature);

            String apparentTemperatureString = String.format(getString(R.string.apparentTemperature), currentWeatherInfo.getApparentTemperature().intValue());
            apparentTemperature.setText(apparentTemperatureString);

            String humidityString = String.format(getString(R.string.humidity), (int) (currentWeatherInfo.getHumidity() * 100));
            humidity.setText(humidityString);

            String precipitationString = String.format(getString(R.string.precipitation), (int) (currentWeatherInfo.getPrecipitationProbability() * 100));
            precipitation.setText(precipitationString);

            String windSpeedString = String.format(getString(R.string.windSpeed), (int) (currentWeatherInfo.getWindSpeed() * 3.6));
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
        } else {
            // Start a progress indicator here as there is no data (this will only happen if the DB is empty, i.e. first launch of the app).
            // In this case, the weather is still busy being retrieved. Set the layout to show refreshing while this is happening.
            swipeToRefreshLayout.setRefreshing(true);
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
