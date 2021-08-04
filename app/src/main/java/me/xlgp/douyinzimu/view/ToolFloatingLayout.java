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

public class ToolFloatingLayout extends BaseFloatingLayout {

    private String zimuFloatinglayout;

    public ToolFloatingLayout(Context context) {
        super(context, R.layout.tool_floating_layout);
        String layoutName = "ToolFloatingLayout";
        super.build(new LayoutParamsWithPoint(new Point(-getFullWidth() / 2, 0)), layoutName);
        viewListener();
    }

    private void viewListener() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        View view = getCurrentLayout();
        view.findViewById(R.id.moveLayoutBtn).setOnTouchListener(new FloatingMoveListener(view, getLayoutParams(), windowManager));

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
        view.findViewById(R.id.pinglunListBtn).setOnClickListener(v -> { //打开字幕列表layout
            if (ZWindowManager.getInstance(getContext()).containView(zimuFloatinglayout)) {
                Toast.makeText(v.getContext(), "字幕列表已存在", Toast.LENGTH_SHORT).show();
                return;
            }
            zimuFloatinglayout = new ZimuMainFloatingLayout(getContext()).getLayoutName();
        });
    }
}
