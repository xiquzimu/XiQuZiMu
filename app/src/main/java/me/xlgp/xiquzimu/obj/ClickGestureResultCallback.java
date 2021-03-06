package me.xlgp.xiquzimu.obj;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;

public class ClickGestureResultCallback extends AccessibilityService.GestureResultCallback {
    private final Callback<Object> callback;

    public ClickGestureResultCallback(Callback<Object> callback) {
        this.callback = callback;
    }

    public ClickGestureResultCallback() {
        this(null);
    }

    @Override
    public void onCompleted(GestureDescription gestureDescription) {
        super.onCompleted(gestureDescription);
        if (callback != null) {
            callback.call(new Object());
        }
    }

    @Override
    public void onCancelled(GestureDescription gestureDescription) {
        super.onCancelled(gestureDescription);
    }
}
