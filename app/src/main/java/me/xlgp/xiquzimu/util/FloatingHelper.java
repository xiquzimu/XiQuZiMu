package me.xlgp.xiquzimu.util;

import android.content.Context;
import android.provider.Settings;

public class FloatingHelper {

    public static boolean enable(Context context) {
        return Settings.canDrawOverlays(context);
    }

}
