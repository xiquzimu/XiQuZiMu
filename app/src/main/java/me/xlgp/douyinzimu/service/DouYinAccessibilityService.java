package me.xlgp.douyinzimu.service;

import android.accessibilityservice.AccessibilityGestureEvent;
import android.accessibilityservice.AccessibilityService;
import android.os.Bundle;
import android.os.Handler;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;

import me.xlgp.douyinzimu.constant.DouYinConstant;
import me.xlgp.douyinzimu.util.AppHelper;
import me.xlgp.douyinzimu.util.DianZanHelper;
import me.xlgp.douyinzimu.util.PingLunHelper;

import static android.view.accessibility.AccessibilityEvent.TYPE_VIEW_CLICKED;

public class DouYinAccessibilityService extends AccessibilityService {


    private AccessibilityNodeInfo findAccessibilityNodeInfo(AccessibilityNodeInfo nodeInfo, String viewId) {
        List<AccessibilityNodeInfo> inputLayoutList = nodeInfo.findAccessibilityNodeInfosByViewId(viewId);
        if (!inputLayoutList.isEmpty()) {
            return inputLayoutList.get(0);
        }
        return null;
    }

    private void openInputLayout(AccessibilityNodeInfo nodeInfo) {
        //[72,2208][372,2255] 说点什么
        AccessibilityNodeInfo node = findAccessibilityNodeInfo(nodeInfo, DouYinConstant.INPUT_TEXT_LAYOUT_ID);
        if (node != null) {
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }

    private void input(AccessibilityNodeInfo nodeInfo) {
        AccessibilityNodeInfo node = nodeInfo.findFocus(AccessibilityNodeInfo.FOCUS_INPUT);
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
        }
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
        if (AppHelper.isDouYinWindows(this)) {
            return getRootInActiveWindow();
        }
        return null;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        switch (eventType) {
            case TYPE_VIEW_CLICKED:
                if (DianZanHelper.isEnabled(this, event)) {
                    DianZanHelper.dianZan(this);
                    return;
                }else if (PingLunHelper.isEnabled(this, event)){

                }
                pinglun();
        }
    }

    @Override
    public void onInterrupt() {

    }


}
