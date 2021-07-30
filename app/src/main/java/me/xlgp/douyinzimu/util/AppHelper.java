package me.xlgp.douyinzimu.util;

import android.accessibilityservice.AccessibilityService;
import android.widget.Toast;

import me.xlgp.douyinzimu.constant.DouYinConstant;

public class AppHelper {

    public static boolean isDouYinWindows(AccessibilityService service) {
        try {
            String rootPackage = service.getRootInActiveWindow().getPackageName().toString();
            if (DouYinConstant.PACKAGE_NAME.equals(rootPackage)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(service, "获取抖音对象失败", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
