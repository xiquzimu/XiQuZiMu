package me.xlgp.douyinzimu.obj;

import android.graphics.PixelFormat;
import android.graphics.Point;
import android.view.WindowManager;

public class ZimuLayoutParams {

    public static class BaseLayoutParams extends WindowManager.LayoutParams {
        public BaseLayoutParams() {
        }
    }

    public static class WithPoint extends WindowManager.LayoutParams {

        public WithPoint() {
            this(new Point(0, 0));
        }

        public WithPoint(Point point) {
            this.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            this.format = PixelFormat.TRANSPARENT;
            this.width = WindowManager.LayoutParams.WRAP_CONTENT;
            this.height = WindowManager.LayoutParams.WRAP_CONTENT;
            this.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            this.x = point.x;
            this.y = point.y;
        }
    }

    public static class WithFullWidth extends WindowManager.LayoutParams {
        public WithFullWidth() {
            this.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            this.format = PixelFormat.TRANSPARENT;
            this.width = WindowManager.LayoutParams.MATCH_PARENT;
            this.height = WindowManager.LayoutParams.WRAP_CONTENT;
            this.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        }
    }

}
