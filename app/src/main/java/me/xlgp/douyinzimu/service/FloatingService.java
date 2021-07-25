package me.xlgp.douyinzimu.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.List;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.listener.FloatingMoveListener;
import me.xlgp.douyinzimu.obj.PingLun;
import me.xlgp.douyinzimu.obj.changduan.ChangeCiList;
import me.xlgp.douyinzimu.obj.changduan.ChangeDuan;
import me.xlgp.douyinzimu.util.FloatingHelper;
import me.xlgp.douyinzimu.view.ZiMuItemAdapter;
import me.xlgp.douyinzimu.zimu.NvfumaChangDuan;
import me.xlgp.douyinzimu.zimu.TianXianPeiChangDuan;

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
            showFloatingWindow(R.layout.floating_layout);
        } else {
            Toast.makeText(this, "已启动悬浮窗", Toast.LENGTH_SHORT).show();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private boolean enableShow() {
        if (mFloatingViewList.size() >= 1) return false;
        return true;
    }

    private View getFloatingLayout(int resource) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(resource, null);
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

    private void viewListener(View view, WindowManager.LayoutParams layoutParams) {
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        view.findViewById(R.id.moveLayoutBtn).setOnTouchListener(new FloatingMoveListener(view, layoutParams, windowManager));
        view.findViewById(R.id.closeBtn).setOnClickListener(v -> {
            closeFloatingWindow();
        });
        ((SwitchMaterial) view.findViewById(R.id.pingLunSwitch)).setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                PingLun.getInstance().start();
            } else {
                PingLun.getInstance().stop();
            }
        });
        view.findViewById(R.id.pingLunBtn).setOnClickListener(v -> {
            if (PingLun.getInstance().disabled() || !PingLunService.getInstance().hasChangeCi()) {
                Toast.makeText(this, "评论不可用或唱词不存在", Toast.LENGTH_SHORT).show();
            }
        });
        view.findViewById(R.id.pinglunListBtn).setOnClickListener(v -> {
            View zimuView = getFloatingLayout(R.layout.floating_zimu_layout);

            View zimuTitleView = zimuView.findViewById(R.id.zimuListTitleBtn);
            WindowManager.LayoutParams zimulayoutParams = createLayoutParams();
            zimuTitleView.setOnTouchListener(new FloatingMoveListener(zimuView, zimulayoutParams, (WindowManager) getSystemService(WINDOW_SERVICE)));

            RecyclerView recyclerView = zimuView.findViewById(R.id.zimu_recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            List list = new ArrayList();
            ChangeDuan changeDuan = new ChangeDuan();
            changeDuan.setChangeCiList(new ChangeCiList());
            list.add(new NvfumaChangDuan().getZhongZhuangYuan());
            list.add(new TianXianPeiChangDuan().getFuQiHuanJia());
            recyclerView.setAdapter(new ZiMuItemAdapter(list));
            ((WindowManager) getSystemService(WINDOW_SERVICE)).addView(zimuView, zimulayoutParams);
            mFloatingViewList.add(zimuView);
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void showFloatingWindow(int resource) {
        if (FloatingHelper.enable(this)) {
            View view = getFloatingLayout(resource);
            WindowManager.LayoutParams layoutParams = createLayoutParams();
            // 将悬浮窗控件添加到WindowManager
            ((WindowManager) getSystemService(WINDOW_SERVICE)).addView(view, layoutParams);
            viewListener(view, layoutParams);
            mFloatingViewList.add(view);
        }
    }

    private void closeFloatingWindow() {
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        while (mFloatingViewList.size() > 0) {
            View view = mFloatingViewList.get(mFloatingViewList.size() - 1);
            windowManager.removeView(view);
            mFloatingViewList.remove(view);
        }
    }

    @Override
    public void onDestroy() {

        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mFloatingViewList.forEach(windowManager::removeView);

    }
}
