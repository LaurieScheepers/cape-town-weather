package co.eventcloud.capetownweather.manager;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import co.eventcloud.capetownweather.MainActivity;
import co.eventcloud.capetownweather.R;

/**
 * This class is responsible for handling weather notifications in the app
 *
 * <p/>
 * Created by Laurie on 2017/08/13.
 */

public class WeatherNotificationManager {

    public static final int LOW_TEMP_BOUNDARY = 15;
    public static final int HIGH_TEMP_BOUNDARY = 25;

    private static final int COLD_NOTIFICATION_ID = 0;
    private static final int HOT_NOTIFICATION_ID = 1;

    /**
     * Sends a notification to the user if the temperature has dropped below 15 deg C
     */
    public static void sendWeatherWarningNotificationIfNecessary(Context context, int currentTemp) {
        if (currentTemp > HIGH_TEMP_BOUNDARY) {
            sendHotWarningNotification(context, currentTemp);
        } else if (currentTemp < LOW_TEMP_BOUNDARY) {
            sendColdWarningNotification(context, currentTemp);
        }
    }

    /**
     * Sends a notification that the temperature has dropped below 15 deg C
     * @param temp the current temperature
     */
    public static void sendColdWarningNotification(Context context, int temp) {
        NotificationManager notificationManager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentText(String.format(context.getString(R.string.coldWarningNotificationText), temp));
        notificationBuilder.setContentTitle(context.getString(R.string.coldWarningNotificationHeading));
        notificationBuilder.setSmallIcon(R.drawable.ic_cold);
        notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_notification_large));
        notificationBuilder.setContentIntent(pendingIntent);

        notificationManager.notify(COLD_NOTIFICATION_ID, notificationBuilder.build());
    }

    /**
     * Sends a notification that the temperature has risen above 25 deg C
     * @param temp the current temperature
     */
    public static void sendHotWarningNotification(Context context, int temp) {
        NotificationManager notificationManager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentText(String.format(context.getString(R.string.hotWarningNotificationText), temp));
        notificationBuilder.setContentTitle(context.getString(R.string.hotWarningNotificationHeading));
        notificationBuilder.setSmallIcon(R.drawable.ic_hot);
        notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_notification_large));
        notificationBuilder.setContentIntent(pendingIntent);

        notificationManager.notify(HOT_NOTIFICATION_ID, notificationBuilder.build());
    }
}
