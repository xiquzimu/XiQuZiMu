package me.xlgp.douyinzimu.util;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.service.DouYinAccessibilityService;

public class AccessibilitySettingsHelper {

    public static void updateAccessibilitySettingBtn(Context context, Button button) {
        if (AccessibilitySettingsHelper.isEnabled(context)) {
            button.setText(R.string.ApenaccesibilityText);
            button.setTextColor(context.getResources().getColor(R.color.white, null));
        } else {
            button.setText(R.string.NoApenaccesibilityText);
            button.setTextColor(context.getResources().getColor(R.color.red, null));
        }
    }

    /**
     * 是否可用，true:表示服务启动，可以使用
     * @param context context
     * @return bool
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
