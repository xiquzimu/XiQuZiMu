package me.xlgp.douyinzimu.service;

import android.accessibilityservice.GestureDescription;
import android.graphics.Rect;

import me.xlgp.douyinzimu.obj.Callback;
import me.xlgp.douyinzimu.obj.ClickGestureDescription;
import me.xlgp.douyinzimu.obj.ClickGestureDescription.Point;
import me.xlgp.douyinzimu.obj.ClickGestureResultCallback;
import me.xlgp.douyinzimu.obj.DianZan;

public class DianZanService {
    private final DianZan dianZan;
    private final DouYinAccessibilityService service;
    private final GestureDescription dianZanGestureDescription;

    public DianZanService() {
        this(DouYinAccessibilityService.getInstance(), null);
    }

    public DianZanService(DouYinAccessibilityService service, DianZan dianZan) {
        this.service = service;

        this.dianZan = dianZan == null ? new DianZan(500) : dianZan;
        dianZanGestureDescription = new DianZanGestureDescription(getPoint()).build();
    }

    private Point getPoint() {
        //不知道为什么点击屏幕底部会出现异常
        return new Point(6, 200);
    }

    /**
     * 点赞事件
     * <p>
     * 先更新点赞数量，再执行模拟点赞
     * 为什么不先点赞再更新呢？
     * 更新点赞数量，
     */
    public void dianZan() {
        if (service == null || !service.canDianZan()) { //此处判断或许应使用代理模式．
            return;
        }
        if (dianZan.isEmpty()) { //先判断能否点赞，
            return;
        }
        dianZan.updateCount();
        //模拟点赞手势
        service.dispatchGesture(dianZanGestureDescription, new DianZanGestureResultCallback((obj) -> dianZan()), null);
    }
}

class DianZanGestureDescription extends ClickGestureDescription {
    public DianZanGestureDescription(Point point) {
        super(point);
    }
}

class DianZanGestureResultCallback extends ClickGestureResultCallback {
    public DianZanGestureResultCallback(Callback<Object> callback) {
        super(callback);
    }
}
