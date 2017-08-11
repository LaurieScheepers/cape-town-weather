package co.eventcloud.capetownweather.weather;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import co.eventcloud.capetownweather.R;
import co.eventcloud.capetownweather.network.WeatherRetriever;
import co.eventcloud.capetownweather.weather.callback.WeatherUpdateListener;
import timber.log.Timber;

/**
 * The service that will handle the API calls done in the background
 *
 * <p/>
 *
 * Created by Laurie on 2017/08/10.
 */

public class WeatherService extends IntentService {

    public static final int LOW_TEMP_BOUNDARY = 15;
    public static final int HIGH_TEMP_BOUNDARY = 25;

    public static final int COLD_NOTIFICATION_ID = 0;
    public static final int HOT_NOTIFICATION_ID = 1;

    public WeatherService() {
        super("WeatherService");
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        Timber.d("Getting the weather from the API");

        if (intent != null) {
            final Context context = this;

            WeatherRetriever.getWeather(context, new WeatherUpdateListener() {
                @Override
                public void onWeatherFinishedUpdating(final int temp) {
                    // Pass the intent back to release the wake lock
                    WeatherBroadcastReceiver.completeWakefulIntent(intent);
                }

                @Override
                public void onWeatherUpdateError(String errorMessage) {
                    Timber.e("Failed to get weather from API: " + errorMessage);
                }
            });
        }
    }

    /**
     * Sends a notification that the temperature has dropped below 15 deg C
     * @param temp the current temperature
     */
    public static void sendColdWarningNotification(Context context, int temp) {
        NotificationManager notificationManager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);

        notificationBuilder.setContentText(String.format(context.getString(R.string.coldWarningNotificationText), temp));
        notificationBuilder.setContentTitle(context.getString(R.string.coldWarningNotificationHeading));
        notificationBuilder.setSmallIcon(R.drawable.ic_cold);
        notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_notification_large));

        notificationManager.notify(COLD_NOTIFICATION_ID, notificationBuilder.build());
    }

    /**
     * Sends a notification that the temperature has risen above 25 deg C
     * @param temp the current temperature
     */
    public static void sendHotWarningNotification(Context context, int temp) {
        NotificationManager notificationManager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);

        notificationBuilder.setContentText(String.format(context.getString(R.string.hotWarningNotificationText), temp));
        notificationBuilder.setContentTitle(context.getString(R.string.hotWarningNotificationHeading));
        notificationBuilder.setSmallIcon(R.drawable.ic_hot);
        notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_notification_large));

        notificationManager.notify(HOT_NOTIFICATION_ID, notificationBuilder.build());
    }
}
