package me.xlgp.douyinzimu.service;

import android.accessibilityservice.AccessibilityGestureEvent;
import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Handler;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;
import android.view.animation.AccelerateInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Random;

import me.xlgp.douyinzimu.constant.DouYinConstant;

import static android.view.accessibility.AccessibilityEvent.TYPE_VIEW_CLICKED;
import static android.view.accessibility.AccessibilityEvent.eventTypeToString;

public class DouYinAccessibilityService extends AccessibilityService {

    private int sendCount = 3;

    private AccessibilityNodeInfo findAccessibilityNodeInfo(AccessibilityNodeInfo nodeInfo, String viewId) {
        List<AccessibilityNodeInfo> inputLayoutList = nodeInfo.findAccessibilityNodeInfosByViewId(viewId);
        if (!inputLayoutList.isEmpty()) {
            return inputLayoutList.get(0);
        }
        return null;
    }

    private void openInputLayout(AccessibilityNodeInfo nodeInfo) {

        AccessibilityNodeInfo node = findAccessibilityNodeInfo(nodeInfo, DouYinConstant.INPUT_TEXT_LAYOUT_ID);
        if (node != null) {
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }

    private void input(AccessibilityNodeInfo nodeInfo) {
        AccessibilityNodeInfo node= nodeInfo.findFocus(AccessibilityNodeInfo.FOCUS_INPUT);
        if (node != null) { //输入数据
            Bundle arguments = new Bundle();
            String[] textList = new String[]{"美", "666", "厉害", "赞"};
            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                    textList[(int) (Math.random() * textList.length)]);
            node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        }
    }

    private void send(AccessibilityNodeInfo nodeInfo) {
        List<AccessibilityNodeInfo> sendNodeInfoList = nodeInfo.findAccessibilityNodeInfosByViewId(DouYinConstant.SEND_IMAGE_ID);
        if (!sendNodeInfoList.isEmpty()) { //发送
            AccessibilityNodeInfo node = sendNodeInfoList.get(0);
            Boolean perform = node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            Toast.makeText(this, "发送" + (perform ? "成功" : "失败"), Toast.LENGTH_SHORT).show();
            sendCount--;
            if (sendCount > 0) {
                loop();
            }
        }
    }

    private void loop() {

        new Handler().postDelayed(() -> {
            System.out.println("loop " + sendCount);
            if (sendCount > 0) {
                pinglun();
            } else {
                sendCount = 3;
            }
        }, 10000);

    }

    private void pinglun() {

        AccessibilityNodeInfo nodeInfo = getDouYinAccessibilityNodeInfo();
        if (nodeInfo == null) {
            Toast.makeText(this, "无法获取抖音对象", Toast.LENGTH_SHORT).show();
            return;
        }
        try {

            openInputLayout(nodeInfo);

            input(nodeInfo);

            send(nodeInfo);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            nodeInfo.recycle();
        }
    }

    private AccessibilityNodeInfo getDouYinAccessibilityNodeInfo() {
        try {
            String rootPackage = getRootInActiveWindow().getPackageName().toString();
            if (DouYinConstant.PACKAGE_NAME.equals(rootPackage)) {
                return getRootInActiveWindow();
            }
            List<AccessibilityWindowInfo> list = getWindows();
            for (AccessibilityWindowInfo info : list) {
                if (info.getRoot() == null) continue;
                if (DouYinConstant.PACKAGE_NAME.equals(info.getRoot().getPackageName())) {
                    return info.getRoot();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private GestureDescription getGestureDescription() {
        Path path = new Path();
        path.moveTo(300, 600);
        return new GestureDescription.Builder().addStroke(new GestureDescription.StrokeDescription(path, 0, 100)).build();

    }


    int dianzanTotalCount = 1000;
    int dianzanCount = dianzanTotalCount;

    private void dianZan() {
        if (dianzanCount <= 0) {
            dianzanCount--;
            dianzanCount = dianzanTotalCount;
            return;
        }

        GestureDescription description = getGestureDescription();
        this.dispatchGesture(description,
                new GestureResultCallback() {
                    @Override
                    public void onCompleted(GestureDescription gestureDescription) {
                        super.onCompleted(gestureDescription);
                        DouYinAccessibilityService.this.dianZan();
                    }

                    @Override
                    public void onCancelled(GestureDescription gestureDescription) {
                        super.onCancelled(gestureDescription);
                        dianzanCount = dianzanTotalCount;
                    }
                }, null);
        dianzanCount--;
    }

    private boolean isDianZan(AccessibilityEvent event) {
        AccessibilityNodeInfo src = event.getSource();
        String zan = "点赞";
        if (src != null && zan.equals(src.getText())) {
            AccessibilityNodeInfo nodeInfo = getDouYinAccessibilityNodeInfo();
            if (nodeInfo == null) return false;
            return true;
        }
        return false;
    }

    private boolean isPingLun(AccessibilityEvent event) {
        AccessibilityNodeInfo src = event.getSource();
        String pinglun = "评论";
        if (src != null && pinglun.equals(src.getText())) {
            AccessibilityNodeInfo nodeInfo = getDouYinAccessibilityNodeInfo();
            if (nodeInfo == null) return false;
            return true;
        }
        return false;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        AccessibilityNodeInfo src = event.getSource();
        String strEventTyp = AccessibilityEvent.eventTypeToString(eventType);
        Toast.makeText(this, "辅助事件类型:" + AccessibilityEvent.eventTypeToString(eventType), Toast.LENGTH_SHORT).show();

        switch (eventType) {
            case TYPE_VIEW_CLICKED:
                if (isDianZan(event)) {
                    dianZan();
                    return;
                }
                pinglun();

        }

    }

    @Override
    public boolean onGesture(@NonNull AccessibilityGestureEvent gestureEvent) {

        return super.onGesture(gestureEvent);
    }

    @Override
    public void onInterrupt() {

    }


}
