package me.xlgp.douyinzimu.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.designpatterns.AllObserver;
import me.xlgp.douyinzimu.designpatterns.ChangDuanObservable;
import me.xlgp.douyinzimu.listener.FloatingMoveListener;
import me.xlgp.douyinzimu.obj.LayoutParamsWithPoint;
import me.xlgp.douyinzimu.obj.changduan.ChangDuan;
import me.xlgp.douyinzimu.service.FloatingService;
import me.xlgp.douyinzimu.service.PingLunService;
import me.xlgp.douyinzimu.util.ChangDuanHelper;

@SuppressLint("ViewConstructor")
public class ZimuFloatinglayout extends BaseFloatingLayout {
    RecyclerView recyclerView = null;
    private WindowManager.LayoutParams layoutParams;
    private boolean isShou = false;
    private View rootLayout = null;
    private String layoutName = "zimuListFloatingLayout";

    public ZimuFloatinglayout(Context context) {
        super(context, R.layout.zimu_floating_layout);
        rootLayout = getCurrentLayout();
        addView();
        initRecyclerView();
        onListener();
    }

    public void addView() {
        this.layoutParams = new LayoutParamsWithPoint(new Point(getFullWidth() / 2, 0));
        super.addViewToWindowManager(layoutParams, layoutName);
    }

    private void onListener() {
        this.rootLayout.findViewById(R.id.zimuListTitleBtn).setOnTouchListener(new FloatingMoveListener(rootLayout, layoutParams, (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)));
        this.rootLayout.findViewById(R.id.closeFloatingBtn).setOnClickListener(v -> {
            FloatingService service = (FloatingService) getContext();
            service.closeFloatingWindow(rootLayout);
        });
        this.rootLayout.findViewById(R.id.clearCurrentZiMuBtn).setOnClickListener(v -> { //清除当前唱段
            PingLunService.getInstance().clear();
            TextView textView = this.rootLayout.findViewById(R.id.currentZimuTitleTextView);
            textView.setText(R.string.currentZimuTitle);
        });
        this.rootLayout.findViewById(R.id.kaiOrShouBtn).setOnClickListener(v -> {
            if (isShou) {
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                isShou = false;
            } else {
                layoutParams.height = rootLayout.getHeight() - recyclerView.getHeight();
                isShou = true;
            }
            ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).updateViewLayout(rootLayout, layoutParams);
        });
    }

    private void initRecyclerView() {
        recyclerView = this.rootLayout.findViewById(R.id.zimu_list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ChangDuanAdapter changDuanAdapter = new ChangDuanAdapter(getChangDuanObservable());
        recyclerView.setAdapter(changDuanAdapter);
        ChangDuanHelper.getChangDuanList(getContext()).subscribe(changDuanAdapter::updateData);
    }

    private ChangDuanObservable<ChangDuan> getChangDuanObservable() {
        ChangDuanObservable<ChangDuan> observable = new ChangDuanObservable<>();
        observable.addObserver(new AllObserver.CurrentZimuItemObserver(this.rootLayout.findViewById(R.id.currentZimuTitleTextView)));
        observable.addObserver(new AllObserver.ChangeCiListObserver());
        return observable;
    }
}
