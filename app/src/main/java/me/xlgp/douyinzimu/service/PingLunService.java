package me.xlgp.douyinzimu.service;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import me.xlgp.douyinzimu.obj.Callback;
import me.xlgp.douyinzimu.util.PingLunHelper;

public class PingLunService {

    //标记是否按当前唱词间隔时间
    public static Integer CURRENT_MILLIS = 0;

    //todo 此处应该重构
    private long count = 0; //记录线程数量，用于判断即将执行的线程是不是当前应当执行的线程

    public void start(long delayMillis) {
        if (count < 0)return;
        count++;
        new Handler(Looper.getMainLooper()).postDelayed(new PinglunRunnable(count), delayMillis);
    }

    public void run(CharSequence content, Callback<Boolean> callback) {
        try {
            PingLunHelper.input(DouYinAccessibilityService.getInstance(), content, callback);
        } catch (Exception e) {
            Log.e("TAG", "run: ", e);
        }
    }

    public void disable() {
        count = -1;
    }

    class PinglunRunnable implements Runnable {
        long count;

        public PinglunRunnable(long count) {
            this.count = count;
        }

        @Override
        public void run() {
            try {
                if (count == PingLunService.this.count) {
                    PingLunHelper.openInputLayout(DouYinAccessibilityService.getInstance());
                }
            } catch (Exception e) {
                Log.e("TAG", "run: ", e);
            }
        }
    }
}
