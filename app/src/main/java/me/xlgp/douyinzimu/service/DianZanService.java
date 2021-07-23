package me.xlgp.douyinzimu.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;

import me.xlgp.douyinzimu.obj.Callback;
import me.xlgp.douyinzimu.obj.ClickGestureDescription;
import me.xlgp.douyinzimu.obj.ClickGestureResultCallback;
import me.xlgp.douyinzimu.obj.DianZan;

public class DianZanService {
    private DianZan dianZan = null;
    private AccessibilityService service = null;
    private GestureDescription dianZanGestureDescription = null;

    public DianZanService(AccessibilityService service) {
        this(service, null);
    }

    public DianZanService(AccessibilityService service, DianZan dianZan) {
        this.service = service;
        this.dianZan = dianZan == null ? new DianZan(500) : dianZan;
        dianZanGestureDescription = new DianZanGestureDescription().build();
    }

    /**
     * 点赞事件
     */
    public void dianZan() {
        if (dianZan.isEmpty()) { //先判断能否点赞，
            return;
        }

        /**
         * 先更新点赞数量，再执行模拟点赞
         *         为什么不先点赞再更新呢？
         *         更新点赞数量，
         */
        dianZan.updateCount();
        //模拟点赞手势
        service.dispatchGesture(dianZanGestureDescription, new DianZanGestureResultCallback((obj) -> dianZan()), null);

    }
}

class DianZanGestureDescription extends ClickGestureDescription {
}

class DianZanGestureResultCallback extends ClickGestureResultCallback {
    public DianZanGestureResultCallback(Callback callback) {
        super(callback);
    }
}
