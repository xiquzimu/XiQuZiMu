package me.xlgp.douyinzimu.util;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.obj.Callback;
import me.xlgp.douyinzimu.obj.PingLun;
import me.xlgp.douyinzimu.obj.changduan.ChangCi;
import me.xlgp.douyinzimu.obj.changduan.ChangCiList;
import me.xlgp.douyinzimu.service.PingLunService;

public class PingLunHelper {

    public static boolean check(Context context, AccessibilityEvent event) {
        AccessibilityNodeInfo src = event.getSource();
        String pinglun = context.getString(R.string.pinglunBtn);
        if (src != null && pinglun.equals(src.getText())) {
            return true;
        }
        return false;
    }

    /**
     * 判断能否评论，
     * １:先判断事件源是否是douyinzimu　app的评论按钮;
     * 2:再判断评论按钮能否响应
     * ３：最后判断当前屏幕是否是douyin
     *
     * @param context
     * @param event
     * @return
     */
    public static boolean isEnabled(Context context, AccessibilityEvent event) {
        return check(context, event) && AppHelper.isDouYinWindows((AccessibilityService) context);
    }

    public static boolean openInputLayout(AccessibilityService service) {
        AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();

        try {
            if (nodeInfo != null) {
                List<AccessibilityNodeInfo> nodeInfoList = nodeInfo.findAccessibilityNodeInfosByText(service.getString(R.string.dy_input_layout_text));
                for (AccessibilityNodeInfo node : nodeInfoList) {
                    if (node.getParent().isClickable()) {
                        node.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (nodeInfo != null) nodeInfo.recycle();
        }
        return false;
    }

    //评论按钮
    private static boolean isInputLayout(Context context, AccessibilityEvent event) {
        if (event.getText().isEmpty()) {
            return false;
        }
        if (context.getString(R.string.dy_input_layout_text).equals(event.getText().get(0))) {
            return true;
        }
        return false;
    }

    /**
     * 获取发送按钮
     *
     * @param inputNodeInfo
     * @return
     */
    private static AccessibilityNodeInfo getSendNodeByInputNode(AccessibilityNodeInfo inputNodeInfo) {
        try {
            return inputNodeInfo.getParent().getParent().getParent().getChild(2);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 输入评论内容
     *
     * @param
     * @param service
     * @param
     * @return
     */
    private static boolean input(AccessibilityService service, ChangCi changCi, Callback callback) {
        AccessibilityNodeInfo node = null;
        try {
            node = service.getRootInActiveWindow().findFocus(AccessibilityNodeInfo.FOCUS_INPUT);
            if (node == null) return false;
            //输入数据
            Bundle arguments = new Bundle();
            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, changCi.getContent());
            boolean setTextSuccess = node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
            if (setTextSuccess) {
                //发送事件
                boolean sendSuccess = getSendNodeByInputNode(node).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                if (sendSuccess) { //发送成功之后，回调
                    callback.call(changCi.getDelayMillis());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (node != null) node.recycle();
        }
        return true;
    }

    private static boolean enablePingLun() {
        return !PingLun.getInstance().disabled() && PingLunService.getInstance().hasChangeCi();
    }

    /**
     * 评论事件
     * 包括多个事件，
     * 0：douyinzimu app中点击评论按钮，发送评论事件
     * 1：点击douyin界面评论按钮，调出输入框
     * 2：douyin界面输入框输入内容
     * 3：点击发送按钮
     */
    public static boolean pingLun(Context context, AccessibilityEvent event) {
        try {
            if (PingLunHelper.isEnabled(context, event) && enablePingLun()) { //事件源：即判断该事件是否为douyinzimu app中评论按钮发出的事件，douyinzimu 的评论按钮
                openInputLayout((AccessibilityService) context); //点击评论按钮，打开输入界面
                return true;
            }
            if (isInputLayout(context, event) && enablePingLun()) { //事件源：是否为douyin界面评论按钮发出的事件，douyin 界面的评论按钮
                ChangCiList changCiList = PingLunService.getInstance().getChangeCiList();
                input((AccessibilityService) context, changCiList.next(), new pinglunCallback(context, changCiList)); //输入评论内容，点击发送
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static class pinglunCallback implements Callback {
        private Context context;
        private long delayMillis = -1;
        private ChangCiList changCiList = null;

        public pinglunCallback(Context context, ChangCiList changCiList) {
            this.context = context;
            this.changCiList = changCiList;
        }

        @Override
        public void call(Object object) {
            if (changCiList.hasNext()) {
                long delayMillis = object == null ? this.delayMillis : (long) object;
                new Handler(Looper.myLooper()).postDelayed(() -> {
                    openInputLayout((AccessibilityService) context);
                }, delayMillis);
            }
        }
    }

}
