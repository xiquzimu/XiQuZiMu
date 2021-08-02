package me.xlgp.douyinzimu.view;

import android.content.Context;
import android.graphics.Point;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.designpatterns.AllObserver;
import me.xlgp.douyinzimu.designpatterns.ChangDuanObservable;
import me.xlgp.douyinzimu.obj.LayoutParamsWithPoint;
import me.xlgp.douyinzimu.obj.changduan.ChangDuan;
import me.xlgp.douyinzimu.service.PingLunService;
import me.xlgp.douyinzimu.util.ChangDuanHelper;

public class ZimuFloatinglayout extends BasePenalLayout {
    RecyclerView recyclerView = null;
    private View rootLayout = null;
    private String layoutName = "zimuListFloatingLayout";

    public ZimuFloatinglayout(Context context) {
        super(context, R.layout.zimu_floating_layout);
        super.build(new LayoutParamsWithPoint(new Point(getFullWidth() / 2, 0)), layoutName);
        rootLayout = getCurrentLayout();
        initRecyclerView();
        onListener();

    }

    private void onListener() {
        this.rootLayout.findViewById(R.id.clearCurrentZiMuBtn).setOnClickListener(v -> { //清除当前唱段
            PingLunService.getInstance().clear();
            TextView textView = this.rootLayout.findViewById(R.id.currentZimuTitleTextView);
            textView.setText(R.string.currentZimuTitle);
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
