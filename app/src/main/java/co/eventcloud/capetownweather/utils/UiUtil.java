package co.eventcloud.capetownweather.utils;

import android.animation.Animator;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import co.eventcloud.capetownweather.utils.callback.FadeInAnimationCompletedCallback;
import co.eventcloud.capetownweather.utils.callback.FadeOutAnimationCompletedCallback;
import timber.log.Timber;

/**
 * Created by root on 2017/08/10.
 */

public class UiUtil {

    /**
     * The duration for one rotation
     */
    private static final int ROTATE_FOREVER_ANIMATION_DURATION = 10000;

    /**
     * Convenience method for fading the specified view in
     */
    public static void fadeViewIn(final View v) {
        fadeViewIn(v, null);
    }

    public static void fadeViewIn(final View v, final FadeInAnimationCompletedCallback fadeInAnimationCompletedCallback) {
        if (v.getVisibility() != View.VISIBLE) {
            ViewPropertyAnimator anim = v.animate();
            if (anim != null) {
                anim.setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (v.getVisibility() != View.VISIBLE) {
                            Timber.d("For some reason the view is not visible after fade in. Setting it to visible.");
                            v.setVisibility(View.VISIBLE);
                        }

                        if (fadeInAnimationCompletedCallback != null) {
                            fadeInAnimationCompletedCallback.onCompleted();
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });

                v.setAlpha(0);
                v.setVisibility(View.VISIBLE);
                anim.alpha(1);
            } else {
                v.setAlpha(1);
                v.setVisibility(View.VISIBLE);
            }
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
    public static void fadeViewOut(final View view, final int duration) {
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
