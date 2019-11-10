package ru.buryachenko.hw_look4films.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import static ru.buryachenko.hw_look4films.utils.Constants.DURATION_DETAILS_ANIMAYION;

public class SomeAnimation {
    public static void doAnimationPushRight(boolean returnBack, int rightSide, View... views) {
        for (View view : views) {
            ObjectAnimator push = ObjectAnimator.ofFloat(view, "translationX", returnBack ? 0 : rightSide);
            AnimatorSet set = new AnimatorSet();
            set.play(push);
            set.setDuration(DURATION_DETAILS_ANIMAYION);
            set.start();
        }
    }

    public static void doAnimationPressPicture(View view) {
        float from = 1F;
        float to = 0.87F;
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, from, to);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, from, to);
        AnimatorSet setForward = new AnimatorSet();
        setForward.playTogether(scaleX, scaleY);
        setForward.setDuration(DURATION_DETAILS_ANIMAYION / 4);
        ObjectAnimator scaleXBack = ObjectAnimator.ofFloat(view, View.SCALE_X, to, from);
        ObjectAnimator scaleYBack = ObjectAnimator.ofFloat(view, View.SCALE_Y, to, from);
        AnimatorSet setBack = new AnimatorSet();
        setBack.playTogether(scaleXBack, scaleYBack);
        setBack.setDuration(DURATION_DETAILS_ANIMAYION / 3);

        AnimatorSet set = new AnimatorSet();
        set.playSequentially(setForward, setBack);
        set.start();
    }

    public static AnimatorSet doAnimationFly(View view, boolean isArrive) {
        float from = 0.0F;
        float to = 1F;
        if (!isArrive) {
            float tmp = from;
            from = to;
            to = tmp;
        }
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, from, to);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, from, to);
        ObjectAnimator rotate = ObjectAnimator.ofFloat(view, View.ROTATION, 0, 360 * (isArrive ? 1 : -1));

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(DURATION_DETAILS_ANIMAYION*2);
        animatorSet.playTogether(scaleX, scaleY, rotate);

        AnimatorSet set = new AnimatorSet();
        set.playSequentially(animatorSet);
        return set;
    }

    public static void doAnimationDetails(boolean returnBack, View view) {
        float from = 1F;
        float to = 0F;
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, View.ALPHA, returnBack ? from : to, returnBack ? to : from);
        AnimatorSet set = new AnimatorSet();
        set.play(alpha);
        set.setDuration(DURATION_DETAILS_ANIMAYION);
        if (returnBack) {
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(View.GONE);
                }
            });
        } else {
            view.setVisibility(View.VISIBLE);
        }
        set.start();
    }

}
