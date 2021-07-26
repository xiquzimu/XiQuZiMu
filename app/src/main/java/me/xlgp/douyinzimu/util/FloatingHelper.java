package me.xlgp.douyinzimu.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;

import me.xlgp.douyinzimu.constant.SysSettingsContant;
import me.xlgp.douyinzimu.service.FloatingService;

public class FloatingHelper {

    public static void open(Activity activity){
        if (!enable(activity)) {
            Toast.makeText(activity, "当前无权限，请授权", Toast.LENGTH_SHORT);
            activity.startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + activity.getPackageName())), SysSettingsContant.FLOATING_CODE);
        }
    }

    public static boolean enable(Context context){
        return Settings.canDrawOverlays(context);
    }
    public static Intent getFloatingIntent(Activity activity){
        return new Intent(activity, FloatingService.class);
    }
    public static ComponentName startService(Activity activity, Intent intent){
        return activity.startService(intent);
    }

}
