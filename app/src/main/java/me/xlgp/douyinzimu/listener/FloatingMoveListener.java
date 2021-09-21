package me.xlgp.douyinzimu.listener;

import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class FloatingMoveListener implements View.OnTouchListener {
    private int x;
    private int y;
    private WindowManager.LayoutParams layoutParams;
    private final WindowManager windowManager;
    private final View moveView;

    public FloatingMoveListener(View moveView, WindowManager.LayoutParams layoutParams, WindowManager windowManager) {
        this.layoutParams = layoutParams;
        this.layoutParams = (WindowManager.LayoutParams) moveView.getLayoutParams();
        this.windowManager = windowManager;
        this.moveView = moveView;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = (int) event.getRawX();
                y = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int nowX = (int) event.getRawX();
                int nowY = (int) event.getRawY();
                int movedX = nowX - x;
                int movedY = nowY - y;
                x = nowX;
                y = nowY;
                layoutParams.x += movedX;
                layoutParams.y += movedY;
                // 更新悬浮窗控件布局
                windowManager.updateViewLayout(moveView, layoutParams);
                break;
            default:
                break;
        }
        return false;
    }
}
