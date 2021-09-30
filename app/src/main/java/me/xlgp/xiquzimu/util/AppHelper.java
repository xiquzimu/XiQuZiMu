package me.xlgp.xiquzimu.util;

import android.accessibilityservice.AccessibilityService;

import me.xlgp.xiquzimu.constant.DouYinConstant;

public class AppHelper {

    public static boolean isDouYinWindows(AccessibilityService service) {
        try {
            String rootPackage = service.getRootInActiveWindow().getPackageName().toString();
            if (DouYinConstant.PACKAGE_NAME.equals(rootPackage)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
