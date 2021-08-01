package me.xlgp.douyinzimu.obj;

import android.graphics.PixelFormat;
import android.graphics.Point;
import android.view.WindowManager;

public class LayoutParamsWithPoint extends WindowManager.LayoutParams {

    public LayoutParamsWithPoint() {
        this(new Point(0, 0));
    }

    public LayoutParamsWithPoint(Point point) {
        this.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        this.format = PixelFormat.TRANSPARENT;
        this.width = WindowManager.LayoutParams.WRAP_CONTENT;
        this.height = WindowManager.LayoutParams.WRAP_CONTENT;
        this.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        this.x = point.x;
        this.y = point.y;
    }
}
