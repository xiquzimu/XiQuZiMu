package me.xlgp.douyinzimu.view;

import android.content.Context;
import android.graphics.Point;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.listener.FloatingMoveListener;
import me.xlgp.douyinzimu.obj.LayoutParamsWithPoint;
import me.xlgp.douyinzimu.obj.PingLun;
import me.xlgp.douyinzimu.obj.ZWindowManager;
import me.xlgp.douyinzimu.service.FloatingService;
import me.xlgp.douyinzimu.service.PingLunService;

public class ToolFloatingLayout extends BaseFloatingLayout {

    private String layoutName = "ToolFloatingLayout";
    private String zimuFloatinglayout;

    public ToolFloatingLayout(Context context) {
        super(context, R.layout.tool_floating_layout);
        addView();
        viewListener();
    }

    public void addView() {
        super.addViewToWindowManager(new LayoutParamsWithPoint(new Point(-getFullWidth() / 2, 0)), layoutName);
    }

    private void viewListener() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        View view = getCurrentLayout();
        view.findViewById(R.id.moveLayoutBtn).setOnTouchListener(new FloatingMoveListener(view, (WindowManager.LayoutParams) view.getLayoutParams(), windowManager));

        //关闭tool悬浮窗
        view.findViewById(R.id.closeFloatingBtn).setOnClickListener(v -> {
            FloatingService floatingService = (FloatingService) getContext();
            floatingService.closeFloatingWindow(null);
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
                Toast.makeText(getContext(), "请开启评论功能", Toast.LENGTH_SHORT).show();
                return;
            } else if (!PingLunService.getInstance().hasChangeCi()) {
                Toast.makeText(getContext(), "请选择唱段", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(getContext(), "开始评论", Toast.LENGTH_SHORT).show();
        });
        view.findViewById(R.id.pinglunListBtn).setOnClickListener(v -> { //打开字幕列表layout
            if (ZWindowManager.getInstance(getContext()).containView(zimuFloatinglayout)) {
                Toast.makeText(v.getContext(), "字幕列表已存在", Toast.LENGTH_SHORT).show();
                return;
            }
            zimuFloatinglayout = new ZimuFloatinglayout(getContext()).getLayoutName();
        });
    }
}
