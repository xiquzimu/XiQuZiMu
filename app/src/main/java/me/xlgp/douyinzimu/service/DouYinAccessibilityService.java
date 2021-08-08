package me.xlgp.douyinzimu.service;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

import me.xlgp.douyinzimu.util.DianZanHelper;
import me.xlgp.douyinzimu.util.PingLunHelper;

import static android.view.accessibility.AccessibilityEvent.TYPE_VIEW_CLICKED;

public class DouYinAccessibilityService extends AccessibilityService {

    private PingLunService pingLunService;

    public DouYinAccessibilityService() {
        pingLunService = PingLunService.getInstance(this);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        switch (event.getEventType()) {
            case TYPE_VIEW_CLICKED:
                if (DianZanHelper.isEnabled(this, event)) {
                    DianZanHelper.dianZan(this);
                    return;
                }
                if (PingLunHelper.pingLun(this, event)) {
                    pingLunService.run();
                }
                break;
        }
    }

    @Override
    public void onInterrupt() {

    }
}
