package me.xlgp.douyinzimu.util;

import android.accessibilityservice.AccessibilityService;

import me.xlgp.douyinzimu.constant.DouYinConstant;

public class AppHelper {

    public static boolean isDouYinWindows(AccessibilityService service) {
        String rootPackage = service.getRootInActiveWindow().getPackageName().toString();
        if (DouYinConstant.PACKAGE_NAME.equals(rootPackage)) {
            return true;
        }
        return false;
    }
}
