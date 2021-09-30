package me.xlgp.xiquzimu.util;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import java.util.List;

import me.xlgp.xiquzimu.service.DouYinAccessibilityService;

public class AccessibilitySettingsHelper {

    /**
     * 是否可用，true:表示服务启动，可以使用
     *
     * @param context context
     * @return bool
     */
    public static boolean isEnabled(Context context) {
        String serviceClassName = DouYinAccessibilityService.class.getName();
        AccessibilityManager manager = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (!manager.isEnabled()) {
            return false;
        }
        List<AccessibilityServiceInfo> enabledServiceList =
                manager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_HAPTIC);
        List<AccessibilityServiceInfo> installedServiceList = manager.getInstalledAccessibilityServiceList();
        try {
            if (enabledServiceList == null || enabledServiceList.isEmpty()) return false;
            for (AccessibilityServiceInfo service : enabledServiceList) {
                if (serviceClassName.equals(service.getResolveInfo().serviceInfo.name)) {
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
                Toast.makeText(context, "系统查询不到辅助服务", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "辅助服务异常", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
