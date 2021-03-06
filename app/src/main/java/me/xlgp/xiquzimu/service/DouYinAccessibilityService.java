package me.xlgp.xiquzimu.service;

import static android.view.accessibility.AccessibilityEvent.TYPE_VIEW_CLICKED;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.os.Handler;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.List;

import me.xlgp.xiquzimu.R;
import me.xlgp.xiquzimu.constant.DouYinConstant;
import me.xlgp.xiquzimu.listener.OnDianZanListener;
import me.xlgp.xiquzimu.util.PingLunHelper;

public class DouYinAccessibilityService extends AccessibilityService implements OnDianZanListener {

    private static DouYinAccessibilityService douYinAccessibilityService;
    private boolean liveable;

    public static DouYinAccessibilityService getInstance() {
        return douYinAccessibilityService;
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        douYinAccessibilityService = this;
        Toast.makeText(this, "请按返回键返回至应用", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == TYPE_VIEW_CLICKED) {
            if (PingLunHelper.pingLun(this, event)) {
                sendBroadcast(getIntent());
            }
        } else if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            new Handler(getMainLooper()).postDelayed(this::isDouYinLive, 1600);
        }
    }

    private Intent getIntent(){
        Intent intent = new Intent();
        intent.setAction(DouYinConstant.INTENT_DY_SERVICE_ACTION);
        intent.putExtra("action", "run");
        return intent;
    }

    @Override
    public void onInterrupt() {

    }

    public void isDouYinLive() {
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
}
