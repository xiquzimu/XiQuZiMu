package me.xlgp.douyinzimu.service;

import static android.view.accessibility.AccessibilityEvent.TYPE_VIEW_CLICKED;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import me.xlgp.douyinzimu.util.DianZanHelper;
import me.xlgp.douyinzimu.util.PingLunHelper;

public class DouYinAccessibilityService extends AccessibilityService {

    private final PingLunService pingLunService;

    public DouYinAccessibilityService() {
        pingLunService = PingLunService.getInstance().builder(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "请按返回键返回至应用", Toast.LENGTH_LONG).show();
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
