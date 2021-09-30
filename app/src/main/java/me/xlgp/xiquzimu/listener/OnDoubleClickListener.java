package me.xlgp.xiquzimu.listener;

import android.view.View;

public abstract class OnDoubleClickListener implements View.OnClickListener {

    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currentTimeMillis = System.currentTimeMillis();
        long DOUBLE_TIME = 400;
        if (currentTimeMillis - lastClickTime < DOUBLE_TIME) {
            doubleClick(v);
        }
        lastClickTime = currentTimeMillis;
    }

    public abstract void doubleClick(View v);
}
