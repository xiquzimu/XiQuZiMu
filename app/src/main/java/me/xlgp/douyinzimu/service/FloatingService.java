package me.xlgp.douyinzimu.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.listener.FloatingMoveListener;
import me.xlgp.douyinzimu.obj.PingLun;
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

    private WindowManager.LayoutParams createLayoutParams(int gravity) {
        // 设置LayoutParam
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        layoutParams.format = PixelFormat.TRANSPARENT;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.gravity = gravity;
        layoutParams.x = 0;
        layoutParams.y = 0;
        return layoutParams;
    }

    private boolean containView(String key) {
        return floatingLayoutMap.containsKey(key) && floatingLayoutMap.get(key) != null;
    }

    private void viewListener(View view, WindowManager.LayoutParams layoutParams) {
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        view.findViewById(R.id.moveLayoutBtn).setOnTouchListener(new FloatingMoveListener(view, layoutParams, windowManager));
        view.findViewById(R.id.closeFloatingBtn).setOnClickListener(v -> {
            closeFloatingWindow(null);
        });
        ((SwitchMaterial) view.findViewById(R.id.pingLunSwitch)).setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                PingLun.getInstance().start();
            } else {
                PingLun.getInstance().stop();
            }
        });
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
            WindowManager.LayoutParams zimulayoutParams = createLayoutParams(Gravity.RIGHT);
            View zimuLayout = new ZimuFloatinglayout(this, zimulayoutParams).getFloatingLayout();
            ((WindowManager) getSystemService(WINDOW_SERVICE)).addView(zimuLayout, zimulayoutParams);
            floatingLayoutMap.put(ZIMU_LIST_FLOATING_LAYOUT, zimuLayout);
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void showFloatingWindow(int resource) {
        if (FloatingHelper.enable(this)) {
            toolFloatingLayout = getFloatingLayout(resource);
            WindowManager.LayoutParams layoutParams = createLayoutParams(Gravity.LEFT);
            // 将悬浮窗控件添加到WindowManager
            ((WindowManager) getSystemService(WINDOW_SERVICE)).addView(toolFloatingLayout, layoutParams);
            viewListener(toolFloatingLayout, layoutParams);
            floatingLayoutMap.put(TOOL_FLOATING_LAYOUT, toolFloatingLayout);
        }
    }

    public void closeFloatingWindow(View view) {
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        if (view == null) {
            Iterator iter = floatingLayoutMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                windowManager.removeView((View) entry.getValue());
            }
            floatingLayoutMap.clear();
        } else if (floatingLayoutMap.containsValue(view)) {
            Collection collection = floatingLayoutMap.values();
            collection.remove(view);
            windowManager.removeView(view);
        }
    }

    @Override
    public void onDestroy() {

        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mFloatingViewList.forEach(windowManager::removeView);

    }
}
