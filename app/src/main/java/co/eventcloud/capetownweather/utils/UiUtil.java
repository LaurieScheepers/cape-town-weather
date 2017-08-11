package co.eventcloud.capetownweather.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

/**
 * Created by root on 2017/08/10.
 */

public class UiUtil {

    /**
     * The duration for one rotation
     */
    private static final int ROTATE_FOREVER_ANIMATION_DURATION = 10000;

    /**
     * Starts a rotating animation that continues forever
     * @param view the view to rotate
     */
    public static void startRotateForever(View view) {
        RotateAnimation rotateAnimation = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(ROTATE_FOREVER_ANIMATION_DURATION);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        view.startAnimation(rotateAnimation);
    }

    public static void stopRotateForever(View view) {
        view.setAnimation(null);
        view.clearAnimation();
    }
}
