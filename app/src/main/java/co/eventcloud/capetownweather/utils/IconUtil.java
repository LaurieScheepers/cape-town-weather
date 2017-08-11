package co.eventcloud.capetownweather.utils;

import android.content.Context;
import android.graphics.Color;

import com.thbs.skycons.library.CloudFogView;
import com.thbs.skycons.library.CloudHvRainView;
import com.thbs.skycons.library.CloudMoonView;
import com.thbs.skycons.library.CloudRainView;
import com.thbs.skycons.library.CloudSnowView;
import com.thbs.skycons.library.CloudSunView;
import com.thbs.skycons.library.CloudThunderView;
import com.thbs.skycons.library.CloudView;
import com.thbs.skycons.library.MoonView;
import com.thbs.skycons.library.SkyconView;
import com.thbs.skycons.library.SunView;
import com.thbs.skycons.library.WindView;

/**
 * Contains helper methods related to icons
 *
 * <p/>
 * Created by Laurie on 2017/08/11.
 */

public class IconUtil {

    /**
     * Returns the appropriate Skycon view depending on what the icon name value is
     */
    public static SkyconView getSkyconView(Context context, String iconName, boolean isAnimated) {

        int strokeColor = Color.BLACK;
        int fillColor = Color.TRANSPARENT;

        switch (iconName) {
            case "clear-day":
                return new SunView(context, !isAnimated, isAnimated, strokeColor, fillColor);
            case "clear-night":
                return new MoonView(context, !isAnimated, isAnimated, strokeColor, fillColor);
            case "partly-cloudy-day":
                return new CloudSunView(context, !isAnimated, isAnimated, strokeColor, fillColor);
            case "partly-cloudy-night":
                return new CloudMoonView(context, !isAnimated, isAnimated, strokeColor, fillColor);
            case "cloudy":
                return new CloudView(context, !isAnimated, isAnimated, strokeColor, fillColor);
            case "rain":
                return new CloudRainView(context, !isAnimated, isAnimated, strokeColor, fillColor);
            case "hail":
            case "sleet":
                return new CloudHvRainView(context, !isAnimated, isAnimated, strokeColor, fillColor);
            case "snow":
                return new CloudSnowView(context, !isAnimated, isAnimated, strokeColor, fillColor);
            case "wind":
                return new WindView(context, !isAnimated, isAnimated, strokeColor, fillColor);
            case "fog":
                return new CloudFogView(context, !isAnimated, isAnimated, strokeColor, fillColor);
            case "thunder":
            case "thunderstorm":
                return new CloudThunderView(context, !isAnimated, isAnimated, strokeColor, fillColor);
            default:
                return new SunView(context, !isAnimated, isAnimated, strokeColor, fillColor);

        }
    }
}
