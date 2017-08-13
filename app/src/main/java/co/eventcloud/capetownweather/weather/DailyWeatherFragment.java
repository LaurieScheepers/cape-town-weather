package co.eventcloud.capetownweather.weather;

import android.os.Bundle;
import android.os.Parcelable;
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
import co.eventcloud.capetownweather.MainActivity;
import co.eventcloud.capetownweather.R;
import co.eventcloud.capetownweather.manager.WeatherManager;
import co.eventcloud.capetownweather.realm.dao.WeatherDao;
import co.eventcloud.capetownweather.realm.model.RealmWeekWeatherInfo;
import co.eventcloud.capetownweather.utils.IconUtil;
import co.eventcloud.capetownweather.weather.adapter.DailyWeatherAdapter;
import co.eventcloud.capetownweather.weather.callback.WeatherUpdateListener;
import co.eventcloud.capetownweather.weather.event.WeatherInfoUpdateErrorEvent;
import co.eventcloud.capetownweather.weather.event.WeatherInfoUpdatedEvent;
import io.realm.Realm;

import static co.eventcloud.capetownweather.MainActivity.SCROLL_STATE_KEY;

/**
 * Fragment that shows the weather day-by-day
 *
 * <p/>
 * Created by root on 2017/08/11.
 */

@SuppressWarnings({"CanBeFinal", "unused"})
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
    @BindView(R.id.buttonTryAgain)
    Button buttonTryAgain;
    @BindView(R.id.errorLayout)
    RelativeLayout errorLayout;

    private Realm realm;

    private RealmWeekWeatherInfo weekWeatherInfo;

    private DailyWeatherAdapter adapter;

    private LinearLayoutManager linearLayoutManager;

    private Parcelable scrollState;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm = Realm.getDefaultInstance();

        weekWeatherInfo = WeatherDao.getWeekWeatherInfo(realm);

        linearLayoutManager = new LinearLayoutManager(getActivity());

        // If no data, request it from the server.
        if (weekWeatherInfo == null) {

            // Check if there's already a request busy, if so, don't do another one.
            // The reasoning behind this is as follows - the API request we do caters for all the weather forecasts (current, hourly and daily)
            // and we are using adapters based on Realm data which will be automatically updated
            // when the underlying data changes, so there's little chance of not getting the data (only if ALL the requests fail will we have no data).
            // The flag is checked to avoid multiple simultaneous requests.
            if (!WeatherManager.busyGettingWeatherFromApi) {
                WeatherManager.getWeather(getContext(), new WeatherUpdateListener() {
                    @Override
                    public void onWeatherFinishedUpdating() {
                        // Get the updated weather info in the DB
                        weekWeatherInfo = WeatherDao.getWeekWeatherInfo(realm);

                        setWeatherInfo();
                    }

                    @Override
                    public void onWeatherUpdateError(String errorMessage) {
                        showErrorView(errorMessage);
                    }
                });
            }

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
                WeatherManager.getWeather(getContext(), new WeatherUpdateListener() {
                    @Override
                    public void onWeatherFinishedUpdating() {
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

        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (swipeToRefresh != null) {
                        swipeToRefresh.setRefreshing(false);
                    }

                    Snackbar.make(getActivity().findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG).show();

                    // If there's no data in the db show the error card
                    if (weekWeatherInfo == null || weekWeatherInfo.getData().size() == 0) {
                        if (swipeToRefresh != null) {
                            swipeToRefresh.setVisibility(View.GONE);
                        }

                        if (errorLayout != null) {
                            errorLayout.setVisibility(View.VISIBLE);
                        }
                    }

                    if (buttonTryAgain != null) {
                        buttonTryAgain.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (swipeToRefresh != null) {
                                    swipeToRefresh.setVisibility(View.VISIBLE);
                                    swipeToRefresh.setRefreshing(true);
                                }

                                WeatherManager.getWeather(getContext(), new WeatherUpdateListener() {
                                    @Override
                                    public void onWeatherFinishedUpdating() {
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
                }
            });
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Save the restored scroll state
        if (savedInstanceState != null) {
            scrollState = savedInstanceState.getParcelable(SCROLL_STATE_KEY);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Restore scroll state
        if (scrollState != null) {
            linearLayoutManager.onRestoreInstanceState(scrollState);
        }
    }

    private void setRecyclerViewAttributes() {
        recyclerView.setHasFixedSize(true); // For better performance

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void setWeatherInfo() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (weekWeatherInfo == null) {
                        if (!WeatherManager.busyGettingWeatherFromApi) {
                            swipeToRefresh.setRefreshing(true);
                            WeatherManager.getWeather(getContext(), null);
                        }

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
            });
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save scroll state
        scrollState = linearLayoutManager.onSaveInstanceState();
        outState.putParcelable(MainActivity.SCROLL_STATE_KEY, scrollState);
    }
}
