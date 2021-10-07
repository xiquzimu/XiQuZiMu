package me.xlgp.xiquzimu.service;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import me.xlgp.xiquzimu.obj.Callback;
import me.xlgp.xiquzimu.util.PingLunHelper;

public class PingLunService {

    //标记是否按当前唱词间隔时间
    public static Integer CURRENT_MILLIS = 0;

    private final Handler handler;

    private Runnable runnable;

    public PingLunService(){
        handler = new Handler(Looper.getMainLooper());
    }

    public void start(long delayMillis) {
        handler.removeCallbacks(runnable);
        runnable = new PinglunRunnable();
        handler.postDelayed(runnable, delayMillis);
    }

    public void run(CharSequence content, Callback<Boolean> callback) {
        try {
            PingLunHelper.input(DouYinAccessibilityService.getInstance(), content, callback);
        } catch (Exception e) {
            Log.e("TAG", "run: ", e);
        }
    }

    public void disable() {
        handler.removeCallbacks(runnable);
    }

    static class PinglunRunnable implements Runnable {

        @Override
        public void run() {
            try {
                PingLunHelper.openInputLayout(DouYinAccessibilityService.getInstance());
            } catch (Exception e) {
                Log.e("TAG", "run: ", e);
            }
        }
    }
}
