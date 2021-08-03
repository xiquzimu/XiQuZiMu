package me.xlgp.douyinzimu.view;

import android.content.Context;
import android.graphics.Point;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Observable;
import java.util.Observer;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.designpatterns.BaseObservable;
import me.xlgp.douyinzimu.obj.LayoutParamsWithPoint;
import me.xlgp.douyinzimu.obj.changduan.ChangDuanInfo;
import me.xlgp.douyinzimu.service.PingLunService;
import me.xlgp.douyinzimu.util.ChangDuanHelper;

public class ZimuFloatinglayout extends BasePanelLayout {
    RecyclerView recyclerView = null;
    private View rootLayout = null;
    private String layoutName = "zimuListFloatingLayout";
    private Context context;

    public ZimuFloatinglayout(Context context) {
        super(context, R.layout.zimu_floating_layout);
        super.build(new LayoutParamsWithPoint(new Point(getFullWidth() / 2, 0)), layoutName);
        init();
        initRecyclerView();
        onListener();
    }

    private void init() {
        rootLayout = getCurrentLayout();
        setPanelTitle("唱段列表");
        this.context = context;
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
        ChangDuanHelper.getChangDuanInfoList(getContext()).subscribe(changDuanAdapter::updateData);
    }

    private ChangDuanObservable getChangDuanObservable() {
        ChangDuanObservable observable = new ChangDuanObservable();
        observable.addObserver(new CurrentZimuItemObserver(this.rootLayout.findViewById(R.id.currentZimuTitleTextView)));
        observable.addObserver(new ChangeCiListObserver(getContext()));
        return observable;
    }

    /**
     * 当先选中唱段观察者，
     */
    private static class ChangeCiListObserver implements Observer {
        private Context context;

        public ChangeCiListObserver(Context context) {
            this.context = context;
        }

        @Override
        public void update(Observable o, Object arg) {
            ChangDuanObservable observable = (ChangDuanObservable) o;
            ChangDuanInfo changDuanInfo = observable.getData();
            Toast.makeText(context, "需要异步获取唱词" + changDuanInfo.getName(), Toast.LENGTH_SHORT).show();
            new ZimuDetailFloatingLayout(context, changDuanInfo);
        }
    }

    class ChangDuanObservable extends BaseObservable<ChangDuanInfo> {
    }

    /**
     * 当前选中的唱段观察者
     */
    private class CurrentZimuItemObserver implements Observer {
        private TextView textView;

        public CurrentZimuItemObserver(TextView textView) {
            this.textView = textView;
        }

        @Override
        public void update(Observable o, Object arg) {
            ChangDuanObservable observable = (ChangDuanObservable) o;
            ChangDuanInfo changDuanInfo = observable.getData();
            textView.setText(changDuanInfo.getName());
        }
    }
}
