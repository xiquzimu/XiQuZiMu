package me.xlgp.xiquzimu.util;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

public class AnimationHelper {
    public static Animation getRotateAnimation(Context context, int layoutId) {
        Animation rotateAnimation = AnimationUtils.loadAnimation(context, layoutId);
        LinearInterpolator interpolator = new LinearInterpolator();
        rotateAnimation.setInterpolator(interpolator);
        return rotateAnimation;
    }
}
