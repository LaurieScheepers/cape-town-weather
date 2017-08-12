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
import co.eventcloud.capetownweather.realm.model.RealmDayInfo;
import co.eventcloud.capetownweather.utils.IconUtil;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import timber.log.Timber;

/**
 * Adapter responsible for handling data based on the Realm Week Weather DB object (which contains a list of Day info objects)
 * <p>
 * <p/>
 * Created by Laurie on 2017/08/11.
 */

public class DailyWeatherAdapter extends RealmRecyclerViewAdapter<RealmDayInfo, RecyclerView.ViewHolder> {

    public DailyWeatherAdapter(@Nullable OrderedRealmCollection<RealmDayInfo> data) {
        super(data, true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_daily_weather, parent, false);

        return new DailyWeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DailyWeatherViewHolder dailyWeatherViewHolder = (DailyWeatherViewHolder) holder;

        final Context context = dailyWeatherViewHolder.date.getContext();

        RealmDayInfo realmDayInfo = getItem(position);

        // This theoretically should never happen, but it if does let's log it to Crashlytics so we know where to look
        if (realmDayInfo == null) {
            Timber.e("ERROR: Can't update the daily weather info");
            Crashlytics.logException(new IllegalAccessException("Error in updating the daily weather info"));
            return;
        }

        // Time is returned by API as seconds since epoch, convert it to millis
        long timeInMillis = realmDayInfo.getTime() * 1000L;

        // Create a date object from the timestamp
        Date date = new Date(timeInMillis);

        // Format the date using English locale and SAST timezone
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE dd/MM", Locale.ENGLISH);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Africa/Johannesburg"));

        dailyWeatherViewHolder.date.setText(simpleDateFormat.format(date));

        dailyWeatherViewHolder.summary.setText(realmDayInfo.getSummary());
        dailyWeatherViewHolder.summary.setSelected(true);

        // Real Max Temp
        int tempMax = realmDayInfo.getTemperatureMax().intValue();
        dailyWeatherViewHolder.realTempMax.setText(String.format(context.getString(R.string.temperature), tempMax));

        // Real Min Temp
        int tempMin = realmDayInfo.getTemperatureMin().intValue();
        dailyWeatherViewHolder.realTempMin.setText(String.format(context.getString(R.string.temperature), tempMin));

        // Skycon!
        String iconString = realmDayInfo.getIcon();

        final SkyconView skyconView = IconUtil.getSkyconView(context, iconString, false);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        skyconView.setLayoutParams(params);

        // First remove any views that might be in the placeholder container
        if (dailyWeatherViewHolder.skyconPlaceholder.getChildCount() > 0) {
            dailyWeatherViewHolder.skyconPlaceholder.removeAllViews();
        }

        // Now add the correct skycon view
        dailyWeatherViewHolder.skyconPlaceholder.addView(skyconView);
    }

    @SuppressWarnings("WeakerAccess")
    public class DailyWeatherViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.summary)
        TextView summary;
        @BindView(R.id.realTempMax)
        TextView realTempMax;
        @BindView(R.id.realTempMin)
        TextView realTempMin;
        @BindView(R.id.tempContainer)
        LinearLayout tempContainer;
        @BindView(R.id.skycon_placeholder)
        FrameLayout skyconPlaceholder;

        public DailyWeatherViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            summary.setSelected(true); // For marquee
        }
    }
}
