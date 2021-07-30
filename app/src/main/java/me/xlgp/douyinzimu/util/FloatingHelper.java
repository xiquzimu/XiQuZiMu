package me.xlgp.douyinzimu.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Button;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.service.FloatingService;

public class FloatingHelper {

    public static boolean enable(Context context) {
        return Settings.canDrawOverlays(context);
    }

    public static Intent getFloatingIntent(Activity activity) {
        return new Intent(activity, FloatingService.class);
    }

    public static ComponentName startService(Activity activity, Intent intent) {
        return activity.startService(intent);
    }

    public static void updateFloatingBtn(Context context, Button button) {
        if (FloatingHelper.enable(context)) {
            button.setText(R.string.openFloatingText);
            button.setTextColor(context.getResources().getColor(R.color.white, null));
        } else {
            button.setText(R.string.no_openFloatingText);
            button.setTextColor(context.getResources().getColor(R.color.red, null));
        }
    }

}
