package me.xlgp.douyinzimu.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import me.xlgp.douyinzimu.MainActivity;
import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.listener.FloatingMoveListener;
import me.xlgp.douyinzimu.util.FloatingHelper;

public class FloatingService extends Service {

    private List<View> mFloatingViewList = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (enableShow()) {
            showFloatingWindow();
        }else {
            Toast.makeText(this, "已启动悬浮窗", Toast.LENGTH_SHORT).show();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private boolean enableShow() {
        if (mFloatingViewList.size() >= 1) return false;
        return true;
    }

    private View createDemoView() {

        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.floating_layout, null);

        return linearLayout;
    }

    private WindowManager.LayoutParams createLayoutParams() {
        // 设置LayoutParam
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        layoutParams.format = PixelFormat.TRANSPARENT;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.x = 0;
        layoutParams.y = 0;
        return layoutParams;
    }

    private void viewListener(View view, WindowManager.LayoutParams layoutParams, WindowManager windowManager){
        Button moveBtn = view.findViewById(R.id.moveLayoutBtn);
        moveBtn.setOnTouchListener(new FloatingMoveListener(view, layoutParams, windowManager));
        Button closeBtn = view.findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                windowManager.removeView(view);
                mFloatingViewList.remove(view);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void showFloatingWindow() {
        if (FloatingHelper.enable(this)) {
            // 获取WindowManager服务
            WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

            WindowManager.LayoutParams layoutParams = createLayoutParams();
            View view = createDemoView();

            viewListener(view, layoutParams, windowManager);

            // 将悬浮窗控件添加到WindowManager
            windowManager.addView(view, layoutParams);

            mFloatingViewList.add(view);
        }
    }

    @Override
    public void onDestroy() {

        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mFloatingViewList.forEach(windowManager::removeView);

    }
}
