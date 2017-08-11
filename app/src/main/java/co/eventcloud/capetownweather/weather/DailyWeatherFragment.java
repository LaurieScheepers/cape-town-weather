package co.eventcloud.capetownweather.weather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import co.eventcloud.capetownweather.R;
import co.eventcloud.capetownweather.network.WeatherRetriever;
import co.eventcloud.capetownweather.realm.dao.WeatherDao;
import co.eventcloud.capetownweather.realm.model.RealmWeekWeatherInfo;
import co.eventcloud.capetownweather.utils.IconUtil;
import co.eventcloud.capetownweather.weather.adapter.DailyWeatherAdapter;
import co.eventcloud.capetownweather.weather.callback.WeatherUpdateListener;
import co.eventcloud.capetownweather.weather.event.WeatherInfoUpdatedEvent;
import io.realm.Realm;

/**
 * Fragment that shows the weather day-by-day
 * Created by root on 2017/08/11.
 */

public class DailyWeatherFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    @BindView(R.id.summary)
    TextView summary;
    @BindView(R.id.skycon_placeholder)
    FrameLayout skyconPlaceholder;
    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout swipeToRefresh;
    private Realm realm;

    private RealmWeekWeatherInfo weekWeatherInfo;

    private DailyWeatherAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm = Realm.getDefaultInstance();

        weekWeatherInfo = WeatherDao.getWeekWeatherInfo(realm);

        // If no data, request it from the server
        if (weekWeatherInfo == null) {

            WeatherRetriever.getWeather(getContext(), new WeatherUpdateListener() {
                @Override
                public void onWeatherFinishedUpdating(final int temp) {
                    // Get the updated weather info in the DB
                    weekWeatherInfo = WeatherDao.getWeekWeatherInfo(realm);

                    setWeatherInfo();
                }
            });

        } else {
            adapter = new DailyWeatherAdapter(weekWeatherInfo.getData());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_weather, container, false);

        unbinder = ButterKnife.bind(this, view);

        setWeatherInfo();

        swipeToRefresh.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorAccent));

        // Set the refresh listener to do the GET weather call if swiped down
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                WeatherRetriever.getWeather(getContext(), new WeatherUpdateListener() {
                    @Override
                    public void onWeatherFinishedUpdating(final int temp) {
                        swipeToRefresh.setRefreshing(false);
                    }
                });
            }
        });

        return view;
    }

    private void setRecyclerViewAttributes() {
        recyclerView.setHasFixedSize(true); // For better performance
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    private void setWeatherInfo() {
        if (weekWeatherInfo == null) {
            WeatherRetriever.getWeather(getContext(), null);
            return;
        }

        if (adapter == null) {
            adapter = new DailyWeatherAdapter(weekWeatherInfo.getData());
        }

        realm = Realm.getDefaultInstance();

        setRecyclerViewAttributes();

        swipeToRefresh.setRefreshing(false);

        // Summary
        summary.setText(weekWeatherInfo.getSummary());
        summary.setSelected(true);

        // Skycon!
        String iconString = weekWeatherInfo.getIcon();

        final SkyconView skyconView = IconUtil.getSkyconView(getContext(), iconString, true);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        skyconView.setLayoutParams(params);

        // First remove any views that might be in the placeholder container
        if (skyconPlaceholder.getChildCount() > 0) {
            skyconPlaceholder.removeAllViews();
        }

        // Now add the correct skycon view
        skyconPlaceholder.addView(skyconView);

        if (!realm.isClosed()) {
            realm.close();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(WeatherInfoUpdatedEvent event) {
        realm = Realm.getDefaultInstance();

        // Get the updated weather info in the DB
        weekWeatherInfo = WeatherDao.getWeekWeatherInfo(realm);

        setWeatherInfo();

        // Now remove the sticky event, we don't want it to be handled again after navigating to this fragment
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
