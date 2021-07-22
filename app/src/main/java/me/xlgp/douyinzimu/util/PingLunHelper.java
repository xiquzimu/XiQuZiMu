package me.xlgp.douyinzimu.util;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.obj.ClickGestureDescription;
import me.xlgp.douyinzimu.obj.ClickGestureResultCallback;

public class PingLunHelper {

    public static boolean check(Context context, AccessibilityEvent event) {
        AccessibilityNodeInfo src = event.getSource();
        String pinglun = context.getString(R.string.pinglunBtn);
        if (src != null && pinglun.equals(src.getText())) {
            return true;
        }
        return false;
    }

    public static boolean isEnabled(Context context, AccessibilityEvent event){
        return check(context, event) && AppHelper.isDouYinWindows((AccessibilityService) context);
    }

    public static void openInputLayout(AccessibilityService service){

        service.dispatchGesture(new ClickGestureDescription().build(), null, null);
    }
}
