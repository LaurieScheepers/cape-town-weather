package co.eventcloud.capetownweather.utils;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import co.eventcloud.capetownweather.utils.callback.FadeInAnimationCompletedCallback;
import co.eventcloud.capetownweather.utils.callback.FadeOutAnimationCompletedCallback;
import timber.log.Timber;

/**
 * Contains helper methods related to UI operations
 *
 * <p/>
 * Created by root on 2017/08/10.
 */

@SuppressWarnings("unused")
public class UiUtil {

    /**
     * Convenience method for fading the specified view in
     */
    public static void fadeViewIn(final View v) {
        fadeViewIn(v, null);
    }

    private static void fadeViewIn(final View v, @SuppressWarnings("SameParameterValue") final FadeInAnimationCompletedCallback callback) {
        if (v == null) {
            Timber.e("Silly, you're trying to fade in a view that does not exist");

            if (callback != null) {
                callback.onCompleted();
            }
            return;
        }

        if (v.getVisibility() != View.VISIBLE) {
            Animation fadeIn = new AlphaAnimation(0, 1);
            fadeIn.setInterpolator(new AccelerateInterpolator());
            fadeIn.setDuration(500);

            fadeIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (v.getVisibility() != View.VISIBLE) {
                        Timber.d("For some reason the view is not visible after fade in. Setting it to visible.");
                        v.setVisibility(View.VISIBLE);
                    }

                    if (callback != null) {
                        callback.onCompleted();
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

            v.startAnimation(fadeIn);
        }
    }

    /**
     * Convenience method for fading the specified view out
     */
    public static void fadeViewOut(final View v) {
        fadeViewOut(v, 500);
    }

    /**
     * Fade out and hide an image view over a specified duration.
     *
     * @param view     The view that will be faded out
     * @param duration The duration in milliseconds of the fade out.
     */
    private static void fadeViewOut(final View view, @SuppressWarnings("SameParameterValue") final int duration) {
        fadeViewOut(view, duration, null);
    }

    /**
     * Fade out and hide an image view over a specified duration.
     *
     * @param view     The view that will be faded out
     * @param duration The duration in milliseconds of the fade out.
     * @param callback Callback that will be called when the animation has completed
     */
    public static void fadeViewOut(final View view, final int duration, @Nullable final FadeOutAnimationCompletedCallback callback) {
        if (view == null) {
            Timber.e("Silly, you're trying to fade in a view that does not exist");

            if (callback != null) {
                callback.onCompleted();
            }
            return;
        }

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(duration);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);

                if (callback != null) {
                    callback.onCompleted();
                }
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });

        view.startAnimation(fadeOut);
    }

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
        if (view != null) {
            view.setAnimation(null);
            view.clearAnimation();
        }
    }
}
