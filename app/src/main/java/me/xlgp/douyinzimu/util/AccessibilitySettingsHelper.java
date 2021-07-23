package me.xlgp.douyinzimu.util;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import java.util.List;

import me.xlgp.douyinzimu.service.DouYinAccessibilityService;

public class AccessibilitySettingsHelper {

    public static void open(Activity activity) {
        if (isEnabled(activity)) {
            return;
        }
        try {
            activity.startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
        } catch (Exception e) {
            activity.startActivity(new Intent(Settings.ACTION_SETTINGS));
            e.printStackTrace();
        }
    }

    /**
     * 是否可用，true:表示服务启动，可以使用
     *
     * @return
     */
    public static boolean isEnabled(Context context) {
        String serviceClassName = DouYinAccessibilityService.class.getName();
        AccessibilityManager manager = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (!manager.isEnabled()) {
            return false;
        }
        List<AccessibilityServiceInfo> enabledServiceList = manager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        List<AccessibilityServiceInfo> installedServiceList = manager.getInstalledAccessibilityServiceList();
        try {
            if (enabledServiceList == null || enabledServiceList.isEmpty()) return false;
            for (AccessibilityServiceInfo service : enabledServiceList) {
                if (serviceClassName.equals(service.getResolveInfo().serviceInfo.name)) {
                    Toast.makeText(context, "服务已启动", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
            boolean isExist = false;
            for (AccessibilityServiceInfo service : installedServiceList) {
                if (serviceClassName.equals(service.getResolveInfo().serviceInfo.name)) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                Toast.makeText(context, "系统查询不到本服务", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "辅助服务异常", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
