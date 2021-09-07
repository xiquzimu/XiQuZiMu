package me.xlgp.douyinzimu.util;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.os.Bundle;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;
import java.util.Objects;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.exception.NotFoundDouYinException;
import me.xlgp.douyinzimu.exception.NotFoundNodeException;
import me.xlgp.douyinzimu.model.ChangCi;
import me.xlgp.douyinzimu.obj.Callback;
import me.xlgp.douyinzimu.service.PingLunService;

public class PingLunHelper {

    /**
     * 点击抖音界面 "说点什么..." view,调出输入框
     */

    public static void openInputLayout(AccessibilityService service) {
        if (!AppHelper.isDouYinWindows(service)) {
            throw new NotFoundDouYinException("获取抖音界面失败");
        }
        if (!PingLunService.getInstance().enablePingLun()) {
            return;
        }
        AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();

        try {
            if (nodeInfo != null) {
                List<AccessibilityNodeInfo> nodeInfoList = nodeInfo.findAccessibilityNodeInfosByText(service.getString(R.string.dy_input_layout_text));
                for (AccessibilityNodeInfo node : nodeInfoList) {
                    if (node.getParent().isClickable()) {
                        node.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        return;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (nodeInfo != null) nodeInfo.recycle();
        }
    }

    //评论按钮
    private static boolean isInputLayout(Context context, List<CharSequence> list) {
        if (list.isEmpty()) {
            return false;
        }
        return context.getString(R.string.dy_input_layout_text).contentEquals(list.get(0));
    }


    private static boolean isSendAccessibilityNodeInfo(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) return false;

        if ("android.widget.ImageView".equals(nodeInfo.getClassName()) && "发送".equals(nodeInfo.getContentDescription()))
            return true;
        return false;
    }

    /**
     * 获取发送按钮
     */
    private static AccessibilityNodeInfo getSendNodeByInputNode(AccessibilityNodeInfo rootNodeInfo) throws Exception {
        AccessibilityNodeInfo node = rootNodeInfo.getParent().getParent().getParent().getChild(2);
        if (isSendAccessibilityNodeInfo(node)) return node;
        throw new NotFoundNodeException("没有找到发送按钮");
    }

    /**
     * 输入评论内容
     *
     * @param service  AccessibilityService
     * @param changCi  唱词
     * @param callback 回调
     */
    public static void input(AccessibilityService service, ChangCi changCi, Callback<Boolean> callback) {
        AccessibilityNodeInfo node = null;

        try {
            node = service.getRootInActiveWindow().findFocus(AccessibilityNodeInfo.FOCUS_INPUT);
            //输入数据
            Bundle arguments = new Bundle();
            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, changCi.getContent());
            boolean setTextSuccess = Objects.requireNonNull(node).performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
            if (setTextSuccess) {
                boolean sendSuccess = getSendNodeByInputNode(node).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                if (sendSuccess) { //发送成功之后，回调
                    callback.call(true);
                }else{
                    throw new RuntimeException("发送操作异常");
                }
            }else{
                throw new RuntimeException("输入内容异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            callback.call(false);
        } finally {
            if (node != null) node.recycle();
        }
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
        return isInputLayout(service, event.getText());
    }
}
