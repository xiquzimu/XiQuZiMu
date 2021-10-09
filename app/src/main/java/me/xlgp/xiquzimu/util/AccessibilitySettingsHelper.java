package me.xlgp.xiquzimu.util;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import me.xlgp.xiquzimu.service.DouYinAccessibilityService;

public class AccessibilitySettingsHelper {

    public static boolean isEnabled(Context context){
        return findBySettingsSecure(context);
    }

    /**
     *     是否可用，true:表示服务启动，可以使用
     * @param context context
     * @return
     */
    private static boolean findBySettingsSecure(Context context){
        int ok = 0;
        try {
            ok = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            Log.e("TAG", "findBySettingsSecure: ", e);
        }

        TextUtils.SimpleStringSplitter ms = new TextUtils.SimpleStringSplitter(':');
        if (ok == 1) {
            String settingValue = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                ms.setString(settingValue);
                while (ms.hasNext()) {
                    String accessibilityService = ms.next();
                    if (accessibilityService.contains(DouYinAccessibilityService.class.getName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
