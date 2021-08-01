package me.xlgp.douyinzimu.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.listener.FloatingMoveListener;
import me.xlgp.douyinzimu.obj.LayoutParamsWithPoint;
import me.xlgp.douyinzimu.obj.PingLun;
import me.xlgp.douyinzimu.obj.ZWindowManager;
import me.xlgp.douyinzimu.util.FloatingHelper;
import me.xlgp.douyinzimu.view.ZimuFloatinglayout;

public class FloatingService extends Service {

    private static final String TOOL_FLOATING_LAYOUT = "toolFloatingLayout";
    private static final String ZIMU_LIST_FLOATING_LAYOUT = "zimuListFloatingLayout";
    private List<View> mFloatingViewList = new ArrayList<>();
    private View toolFloatingLayout = null;
    private Map<String, View> floatingLayoutMap = new HashMap<>();
    private int viewCount = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!containView(TOOL_FLOATING_LAYOUT)) {
            showFloatingWindow(R.layout.tool_floating_layout);
        } else {
            Toast.makeText(this, "已启动悬浮窗", Toast.LENGTH_SHORT).show();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private View getFloatingLayout(int resource) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(resource, null);
    }

    private int getWidth() {
        int width;
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = windowManager.getCurrentWindowMetrics();
            WindowMetrics maxe = windowManager.getMaximumWindowMetrics();
            maxe.getBounds().centerX();
            width = windowMetrics.getBounds().centerX();
        } else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            width = displayMetrics.widthPixels;
        }
        return width;
    }

    /**
     * @param direction 方向，主要用于设置窗口位置，+1：靠右边，-1：靠左边
     * @return WindowManager.LayoutParams
     */
    private WindowManager.LayoutParams createLayoutParams(int direction) {
        return new LayoutParamsWithPoint(new Point((getWidth()) / 2 * direction, 0));
    }

    private boolean containView(String key) {
        return floatingLayoutMap.containsKey(key) && floatingLayoutMap.get(key) != null;
    }

    private void viewListener(View view, WindowManager.LayoutParams layoutParams) {
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        view.findViewById(R.id.moveLayoutBtn).setOnTouchListener(new FloatingMoveListener(view, layoutParams, windowManager));

        //关闭tool悬浮窗
        view.findViewById(R.id.closeFloatingBtn).setOnClickListener(v -> {
            closeFloatingWindow(null);
            stopSelf();
        });
        //评论控制器
        ((SwitchMaterial) view.findViewById(R.id.pingLunSwitch)).setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                PingLun.getInstance().start();
            } else {
                PingLun.getInstance().stop();
            }
        });
        //开始评论
        view.findViewById(R.id.pingLunBtn).setOnClickListener(v -> {
            if (PingLun.getInstance().disabled()) {
                Toast.makeText(this, "请开启评论功能", Toast.LENGTH_SHORT).show();
                return;
            } else if (!PingLunService.getInstance().hasChangeCi()) {
                Toast.makeText(this, "请选择唱段", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "开始评论", Toast.LENGTH_SHORT).show();
        });
        view.findViewById(R.id.pinglunListBtn).setOnClickListener(v -> { //打开字幕列表layout
            if (containView(ZIMU_LIST_FLOATING_LAYOUT)) {
                Toast.makeText(v.getContext(), "字幕列表已存在", Toast.LENGTH_SHORT).show();
                return;
            }
            WindowManager.LayoutParams zimulayoutParams = createLayoutParams(1);
            View zimuLayout = new ZimuFloatinglayout(this, zimulayoutParams).getFloatingLayout();
            ZWindowManager.getInstance(null).addView(zimuLayout, zimulayoutParams, ZIMU_LIST_FLOATING_LAYOUT);

        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void showFloatingWindow(int resource) {
        if (FloatingHelper.enable(this)) {
            toolFloatingLayout = getFloatingLayout(resource);
            WindowManager.LayoutParams layoutParams = createLayoutParams(-1);
            // 将悬浮窗控件添加到WindowManager
            ZWindowManager.getInstance((WindowManager) getSystemService(WINDOW_SERVICE)).addView(toolFloatingLayout, layoutParams, TOOL_FLOATING_LAYOUT);
            viewListener(toolFloatingLayout, layoutParams);
        }
    }

    public void closeFloatingWindow(View view) {
        ZWindowManager zWindowManager = ZWindowManager.getInstance(null);
        if (view == null) {
            zWindowManager.removeAllView();
        } else {
            zWindowManager.removeView(view);
        }
    }

    @Override
    public void onDestroy() {
        ZWindowManager zWindowManager = ZWindowManager.getInstance(null);
        zWindowManager.removeAllView();
    }
}
