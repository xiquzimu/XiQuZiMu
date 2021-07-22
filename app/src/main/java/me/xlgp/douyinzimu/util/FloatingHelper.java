package me.xlgp.douyinzimu.util;

import android.app.Activity;
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

    public static void startService(Activity activity){
        activity.startService(new Intent(activity, FloatingService.class));
    }

    public static void openAndStart(Activity activity){
        if (!enable(activity)) {
            open(activity);
        }else{
            startService(activity);
        }
    }
    public static void resultAndStart(Activity activity){
        if (!enable(activity)) {
            Toast.makeText(activity, "授权失败", Toast.LENGTH_SHORT).show();
        } else {
            startService(activity);
            Toast.makeText(activity, "授权成功", Toast.LENGTH_SHORT).show();
        }
    }
}
