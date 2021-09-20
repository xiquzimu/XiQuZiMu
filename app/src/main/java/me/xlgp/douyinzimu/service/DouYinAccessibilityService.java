package me.xlgp.douyinzimu.service;

import android.accessibilityservice.AccessibilityService;
import android.os.Handler;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.List;
import java.util.Observer;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.designpatterns.BaseObservable;
import me.xlgp.douyinzimu.listener.OnDianZanListener;
import me.xlgp.douyinzimu.util.PingLunHelper;

import static android.view.accessibility.AccessibilityEvent.TYPE_VIEW_CLICKED;

public class DouYinAccessibilityService extends AccessibilityService implements OnDianZanListener {

    private static DouYinAccessibilityService douYinAccessibilityService;
    private DouYinObservable observable;
    private PingLunService pingLunService;
    private boolean liveable;

    public static DouYinAccessibilityService getInstance() {
        return douYinAccessibilityService;
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        douYinAccessibilityService = this;
        observable = new DouYinObservable();
        pingLunService = PingLunService.getInstance();
        Toast.makeText(this, "请按返回键返回至应用", Toast.LENGTH_LONG).show();
    }

    public void addObserver(Observer observer) {
        observable.addObserver(observer);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == TYPE_VIEW_CLICKED) {
            if (PingLunHelper.pingLun(this, event)) {
                pingLunService.run();
            }
        } else if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            new Handler(getMainLooper()).postDelayed(() -> observable.setData(isDouYinLive()), 1600);
        }
    }

    @Override
    public void onInterrupt() {

    }

    private boolean isDouYinLive() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        try {
            List<AccessibilityNodeInfo> nodeInfoList =
                    nodeInfo.findAccessibilityNodeInfosByText(getString(R.string.dy_input_layout_text));
            liveable = nodeInfoList != null && !nodeInfoList.isEmpty();
        } catch (Exception e) {
            liveable = false;
        } finally {
            if (nodeInfo != null) nodeInfo.recycle();
        }
        return liveable;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        douYinAccessibilityService = null;
        liveable = false;
    }

    @Override
    public boolean canDianZan() {
        if (!liveable) isDouYinLive();
        return liveable;
    }

    static class DouYinObservable extends BaseObservable<Boolean> {
    }
}
