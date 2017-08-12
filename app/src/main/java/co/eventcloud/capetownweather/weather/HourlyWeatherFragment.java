package co.eventcloud.capetownweather.weather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import co.eventcloud.capetownweather.realm.model.RealmDayWeatherInfo;
import co.eventcloud.capetownweather.utils.IconUtil;
import co.eventcloud.capetownweather.weather.adapter.HourlyWeatherAdapter;
import co.eventcloud.capetownweather.weather.callback.WeatherUpdateListener;
import co.eventcloud.capetownweather.weather.event.WeatherInfoUpdateErrorEvent;
import co.eventcloud.capetownweather.weather.event.WeatherInfoUpdatedEvent;
import io.realm.Realm;

/**
 * Fragment that shows the weather hour-by-hour
 *
 * <p/>
 * Created by Laurie on 2017/08/11.
 */

public class HourlyWeatherFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    @BindView(R.id.summary)
    TextView summary;
    @BindView(R.id.skycon_placeholder)
    FrameLayout skyconPlaceholder;
    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout swipeToRefresh;
    @BindView(R.id.buttonTryAgain)
    Button buttonTryAgain;
    @BindView(R.id.errorLayout)
    RelativeLayout errorLayout;
    private Realm realm;

    private RealmDayWeatherInfo dayWeatherInfo;

    private HourlyWeatherAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm = Realm.getDefaultInstance();

        dayWeatherInfo = WeatherDao.getDayWeatherInfo(realm);

        // If no data, request it from the server.
        if (dayWeatherInfo == null) {

            // Check if there's already a request busy, if so, don't do another one.
            // The reasoning behind this is as follows - the API request we do caters for all the weather forecasts (current, hourly and daily)
            // and we are using adapters based on Realm data which will be automatically updated
            // when the underlying data changes, so there's little chance of not getting the data (only if ALL the requests fail will we have no data).
            // The flag is checked to avoid multiple simultaneous requests.
            if (!WeatherRetriever.busyGettingWeatherFromApi) {
                WeatherRetriever.getWeather(getContext(), new WeatherUpdateListener() {
                    @Override
                    public void onWeatherFinishedUpdating(final int temp) {
                        // Get the updated weather info in the DB
                        dayWeatherInfo = WeatherDao.getDayWeatherInfo(realm);

                        setWeatherInfo();
                    }

                    @Override
                    public void onWeatherUpdateError(String errorMessage) {
                        showErrorView(errorMessage);
                    }
                });
            }
        } else {
            adapter = new HourlyWeatherAdapter(dayWeatherInfo.getData());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hourly_weather, container, false);

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

                    @Override
                    public void onWeatherUpdateError(String errorMessage) {
                        showErrorView(errorMessage);
                    }
                });
            }
        });

        return view;
    }

    private void showErrorView(final String errorMessage) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (swipeToRefresh != null) {
                    swipeToRefresh.setRefreshing(false);
                }

                Snackbar.make(getActivity().findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG).show();

                // If there's no data in the db show the error card
                if (dayWeatherInfo == null || dayWeatherInfo.getData().size() == 0) {
                    if (swipeToRefresh != null) {
                        swipeToRefresh.setVisibility(View.GONE);
                    }

                    errorLayout.setVisibility(View.VISIBLE);
                }

                buttonTryAgain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (swipeToRefresh != null) {
                            swipeToRefresh.setVisibility(View.VISIBLE);
                            swipeToRefresh.setRefreshing(true);
                        }

                        WeatherRetriever.getWeather(getContext(), new WeatherUpdateListener() {
                            @Override
                            public void onWeatherFinishedUpdating(int temp) {
                                if (errorLayout != null) {
                                    errorLayout.setVisibility(View.GONE);
                                }

                                if (swipeToRefresh != null) {
                                    if (swipeToRefresh.getVisibility() == View.GONE) {
                                        swipeToRefresh.setVisibility(View.VISIBLE);
                                    }

                                    swipeToRefresh.setRefreshing(false);
                                }

                                setWeatherInfo();
                            }

                            @Override
                            public void onWeatherUpdateError(String errorMessage) {
                                showErrorView(errorMessage);
                            }
                        });
                    }
                });
            }
        });
    }

    private void setRecyclerViewAttributes() {
        recyclerView.setHasFixedSize(true); // For better performance
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    private void setWeatherInfo() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dayWeatherInfo == null) {
                    if (!WeatherRetriever.busyGettingWeatherFromApi) {
                        swipeToRefresh.setRefreshing(true);
                        WeatherRetriever.getWeather(getContext(), null);
                    }

                    return;
                }

                if (adapter == null) {
                    adapter = new HourlyWeatherAdapter(dayWeatherInfo.getData());
                }

                realm = Realm.getDefaultInstance();

                setRecyclerViewAttributes();

                swipeToRefresh.setRefreshing(false);

                // Summary
                summary.setText(dayWeatherInfo.getSummary());
                summary.setSelected(true);

                // Skycon!
                String iconString = dayWeatherInfo.getIcon();

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
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(WeatherInfoUpdatedEvent event) {
        realm = Realm.getDefaultInstance();

        // Get the updated weather info in the DB
        dayWeatherInfo = WeatherDao.getDayWeatherInfo(realm);

        setWeatherInfo();

        // Now remove the sticky event, we don't want it to be handled again after navigating to this fragment
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(WeatherInfoUpdateErrorEvent event) {
        showErrorView(getString(R.string.error_generic));

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
