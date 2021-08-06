package me.xlgp.douyinzimu.util;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.os.Bundle;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.obj.Callback;
import me.xlgp.douyinzimu.obj.PingLun;
import me.xlgp.douyinzimu.obj.changduan.ChangCi;
import me.xlgp.douyinzimu.obj.changduan.ChangCiList;
import me.xlgp.douyinzimu.service.PingLunService;

public class PingLunHelper {

    public static boolean check(AccessibilityService context, AccessibilityEvent event) {
        AccessibilityNodeInfo src = event.getSource();
        String pinglun = context.getString(R.string.pinglunBtn);
        return src != null && pinglun.equals(src.getText());
    }

    /**
     * 判断能否评论，
     * １:先判断事件源是否是douyinzimu　app的评论按钮;
     * 2:再判断评论按钮能否响应
     * ３：最后判断当前屏幕是否是douyin
     */
    public static boolean isEnabled(AccessibilityService service, AccessibilityEvent event) {
        return check(service, event) && AppHelper.isDouYinWindows(service);
    }

    public static boolean openInputLayout(AccessibilityService service) {
        if (!enablePingLun()) {
            return false;
        }
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
        return context.getString(R.string.dy_input_layout_text).contentEquals(event.getText().get(0));
    }

    /**
     * 获取发送按钮
     */
    private static AccessibilityNodeInfo getSendNodeByInputNode(AccessibilityNodeInfo inputNodeInfo) {
        try {
            return inputNodeInfo.getParent().getParent().getParent().getChild(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 输入评论内容
     *
     * @param service  AccessibilityService
     * @param changCi  唱词
     * @param callback 回调
     */
    private static void input(AccessibilityService service, ChangCi changCi, Callback<Long> callback) {
        AccessibilityNodeInfo node = null;
        try {
            node = service.getRootInActiveWindow().findFocus(AccessibilityNodeInfo.FOCUS_INPUT);
            if (node == null) return;
            //输入数据
            Bundle arguments = new Bundle();
            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, changCi.getContent());
            boolean setTextSuccess = node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
            if (setTextSuccess) {
                //发送事件
                boolean sendSuccess = Objects.requireNonNull(getSendNodeByInputNode(node)).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                if (sendSuccess) { //发送成功之后，回调
                    callback.call(changCi.getDelayMillis());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (node != null) node.recycle();
        }
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
    public static boolean pingLun(AccessibilityService service, AccessibilityEvent event) {
        try {
            ChangCiList changCiList = PingLunService.getInstance().getChangDuan().getChangeCiList();
            //事件源：即判断该事件是否为douyinzimu app中评论按钮发出的事件，douyinzimu 的评论按钮
            if (PingLunHelper.isEnabled(service, event) && enablePingLun()) {
                //点击评论按钮，打开输入界面
                asyncOpenInputLayout(service, changCiList.current().getDelayMillis());
                return true;
            }
            //事件源：是否为douyin界面评论按钮发出的事件，douyin 界面的评论按钮
            if (isInputLayout(service, event) && enablePingLun()) {
                input(service, changCiList.next(), new PinglunCallback(service)); //输入评论内容，点击发送
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void asyncOpenInputLayout(AccessibilityService service, long delayMillis) {
        Observable.timer(delayMillis, TimeUnit.MILLISECONDS).map(o -> openInputLayout(service)).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    private static class PinglunCallback implements Callback<Long> {
        private final AccessibilityService service;

        public PinglunCallback(AccessibilityService service) {
            this.service = service;
        }

        @Override
        public void call(Long delay) {
            long delayMillis = delay == null ? -1 : delay;
            asyncOpenInputLayout(service, delayMillis);
        }
    }
}
