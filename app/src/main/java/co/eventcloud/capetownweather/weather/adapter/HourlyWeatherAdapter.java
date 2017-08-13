package co.eventcloud.capetownweather.weather.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.thbs.skycons.library.SkyconView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.eventcloud.capetownweather.R;
import co.eventcloud.capetownweather.realm.model.RealmHourInfo;
import co.eventcloud.capetownweather.utils.IconUtil;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import timber.log.Timber;

/**
 * Adapter responsible for handling data based on the Realm Day Weather DB object (which contains a list of Hour info objects)
 *
 * <p/>
 * Created by Laurie on 2017/08/11.
 */

public class HourlyWeatherAdapter extends RealmRecyclerViewAdapter<RealmHourInfo, RecyclerView.ViewHolder> {

    public HourlyWeatherAdapter(@Nullable OrderedRealmCollection<RealmHourInfo> data) {
        super(data, true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_hourly_weather, parent, false);

        return new HourlyWeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HourlyWeatherViewHolder hourlyWeatherViewHolder = (HourlyWeatherViewHolder) holder;

        final Context context = hourlyWeatherViewHolder.time.getContext();

        RealmHourInfo realmHourInfo = getItem(position);

        // This theoretically should never happen, but it if does let's log it to Crashlytics so we know where to look
        if (realmHourInfo == null) {
            Timber.e("ERROR: Can't update the hourly weather info");
            Crashlytics.logException(new IllegalAccessException("Error in updating the hourly weather info"));
            return;
        }

        // Time is returned by API as seconds since epoch, convert it to millis
        long timeInMillis = realmHourInfo.getTime() * 1000L;

        // Create a date object from the timestamp
        Date date = new Date(timeInMillis);

        // Format the date using English locale and SAST timezone
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE HH:mm", Locale.ENGLISH);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Africa/Johannesburg"));

        hourlyWeatherViewHolder.time.setText(simpleDateFormat.format(date));

        hourlyWeatherViewHolder.summary.setText(realmHourInfo.getSummary());
        hourlyWeatherViewHolder.summary.setSelected(true);

        // Real Temp
        String temperature = String.format(context.getString(R.string.temperature), realmHourInfo.getTemperature().intValue());
        hourlyWeatherViewHolder.realTemp.setText(temperature);

        // Skycon!
        String iconString = realmHourInfo.getIcon();

        final SkyconView skyconView = IconUtil.getSkyconView(context, iconString, false);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        skyconView.setLayoutParams(params);

        // First remove any views that might be in the placeholder container
        if (hourlyWeatherViewHolder.skyconPlaceholder.getChildCount() > 0) {
            hourlyWeatherViewHolder.skyconPlaceholder.removeAllViews();
        }

        // Now add the correct skycon view
        hourlyWeatherViewHolder.skyconPlaceholder.addView(skyconView);
    }

    class HourlyWeatherViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.summary)
        TextView summary;
        @BindView(R.id.skycon_placeholder)
        FrameLayout skyconPlaceholder;
        @BindView(R.id.realTemp)
        TextView realTemp;

        HourlyWeatherViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            summary.setSelected(true); // For marquee
        }
    }
}
