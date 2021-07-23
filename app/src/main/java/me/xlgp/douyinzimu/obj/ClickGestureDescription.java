package me.xlgp.douyinzimu.obj;

import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.graphics.PointF;

public class ClickGestureDescription {
    private Point point = null;

    public ClickGestureDescription(Point point) {
        this.point = point;
    }
    public ClickGestureDescription(){
        this.point = new Point(300,550);
    }

    public GestureDescription build() {
        return new GestureDescription.Builder().addStroke(new GestureDescription.StrokeDescription(getPath(), 0, 100)).build();
    }

    private Path getPath() {
        Path path = new Path();
        path.moveTo(this.point.x, this.point.y);
        return path;
    }

    public static class Point extends PointF {

        public Point(float x, float y) {
            super(x, y);
        }
    }
}
