package me.xlgp.xiquzimu.util;

import android.accessibilityservice.AccessibilityService;
import android.os.Bundle;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;
import java.util.Objects;

import me.xlgp.xiquzimu.R;
import me.xlgp.xiquzimu.exception.NotFoundNodeException;
import me.xlgp.xiquzimu.obj.Callback;

public class PingLunHelper {

    /**
     * 点击抖音界面 "说点什么..." view,调出输入框
     */

    public static void openInputLayout(AccessibilityService service) {

        AccessibilityNodeInfo nodeInfo = null;
        try {
            nodeInfo = service.getRootInActiveWindow();
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

    private static boolean isSendAccessibilityNodeInfo(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) return false;
        return "android.widget.ImageView".contentEquals(nodeInfo.getClassName()) && "发送".contentEquals(nodeInfo.getContentDescription());
    }

    /**
     * 获取发送按钮
     */
    private static AccessibilityNodeInfo getSendNodeByInputNode(AccessibilityNodeInfo rootNodeInfo) throws Exception {
        AccessibilityNodeInfo node = null;
        try {
            node = rootNodeInfo.getParent().getParent().getParent();
            //douyin v17.9.0
            if (isSendAccessibilityNodeInfo(node.getChild(1))) return node.getChild(1);
            //douyin <= 17.8.0
            if (isSendAccessibilityNodeInfo(node.getChild(2))) return node.getChild(2);
        } catch (Exception e) {
            throw new NotFoundNodeException("没有找到发送按钮");
        } finally {
            if (node != null) node.recycle();
        }
        throw new NotFoundNodeException("没有找到发送按钮");
    }

    /**
     * 输入评论内容
     *
     * @param service  AccessibilityService
     * @param content  唱词
     * @param callback 回调
     */
    public static void input(AccessibilityService service, CharSequence content, Callback<Boolean> callback) {
        if (service == null) {
            callback.call(false);
            return;
        }
        AccessibilityNodeInfo inputNode = null;

        try {
            inputNode = service.getRootInActiveWindow().findFocus(AccessibilityNodeInfo.FOCUS_INPUT);
            //输入数据
            Bundle arguments = new Bundle();
            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, content);
            boolean setTextSuccess = Objects.requireNonNull(inputNode).performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
            if (setTextSuccess) {
                boolean sendSuccess = getSendNodeByInputNode(inputNode).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                if (sendSuccess) { //发送成功之后，回调
                    callback.call(true);
                } else {
                    throw new RuntimeException("发送操作异常");
                }
            } else {
                throw new RuntimeException("输入内容异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            callback.call(false);
        } finally {
            if (inputNode != null) inputNode.recycle();
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
        List<CharSequence> list = event.getText();
        if (list.isEmpty()) {
            return false;
        }
        return service.getString(R.string.dy_input_layout_text).contentEquals(list.get(0));
    }
}
