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
    public static SkyconView getSkyconView(Context context, String iconName) {

        switch (iconName) {
            case "clear-day":
                return new SunView(context, false, true, Color.parseColor("#000000"), Color.parseColor("#ffffff"));
            case "clear-night":
                return new MoonView(context, false, true, Color.parseColor("#000000"), Color.parseColor("#ffffff"));
            case "partly-cloudy-day":
                return new CloudSunView(context, false, true, Color.parseColor("#000000"), Color.parseColor("#ffffff"));
            case "partly-cloudy-night":
                return new CloudMoonView(context, false, true, Color.parseColor("#000000"), Color.parseColor("#ffffff"));
            case "cloudy":
                return new CloudView(context, false, true, Color.parseColor("#000000"), Color.parseColor("#ffffff"));
            case "rain":
                return new CloudRainView(context, false, true, Color.parseColor("#000000"), Color.parseColor("#ffffff"));
            case "hail":
            case "sleet":
                return new CloudHvRainView(context, false, true, Color.parseColor("#000000"), Color.parseColor("#ffffff"));
            case "snow":
                return new CloudSnowView(context, false, true, Color.parseColor("#000000"), Color.parseColor("#ffffff"));
            case "wind":
                return new WindView(context, false, true, Color.parseColor("#000000"), Color.parseColor("#ffffff"));
            case "fog":
                return new CloudFogView(context, false, true, Color.parseColor("#000000"), Color.parseColor("#ffffff"));
            case "thunder":
            case "thunderstorm":
                return new CloudThunderView(context, false, true, Color.parseColor("#000000"), Color.parseColor("#ffffff"));
            default:
                return new SunView(context, false, true, Color.parseColor("#000000"), Color.parseColor("#ffffff"));

        }
    }
}
