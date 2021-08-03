package me.xlgp.douyinzimu.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.designpatterns.BaseObservable;
import me.xlgp.douyinzimu.obj.LayoutParamsWithPoint;
import me.xlgp.douyinzimu.obj.PingLun;
import me.xlgp.douyinzimu.obj.changduan.ChangCi;
import me.xlgp.douyinzimu.obj.changduan.ChangCiList;
import me.xlgp.douyinzimu.obj.changduan.ChangDuan;
import me.xlgp.douyinzimu.service.PingLunService;

public class ZimuDetailFloatingLayout extends BasePanelLayout {
    private final String LAYOUT_NAME = "zimu_detail_layout";
    private RecyclerView recyclerView = null;
    private View rootLayout;

    public ZimuDetailFloatingLayout(Context context) {
        super(context, R.layout.zimu_detail_layout);
        super.build(new LayoutParamsWithPoint(), LAYOUT_NAME);
        init();
        onViewListener();
        initRecyclerView();
    }

    private void init() {
        ChangDuan changDuan = Objects.requireNonNull(PingLunService.getInstance().getChangDuan());
        setPanelTitle(changDuan.getChangeDuanQiTa().getTitle());
        setChangCiListObservable();
        this.rootLayout = getCurrentLayout();
    }

    private void setChangCiListObservable() {
        ChangCiList.ChangCiListObservable changCiListObservable = new ChangCiList.ChangCiListObservable();
        changCiListObservable.addObserver(new ZimuDetailFloatingLayout.ChangCiListObservar());
        PingLunService.getInstance().getChangDuan().getChangeCiList().setChangCiListObservable(changCiListObservable);
    }

    private void onViewListener() {
        //开始评论
        this.rootLayout.findViewById(R.id.startPinglunBtn).setOnClickListener(v -> {
            if (PingLun.getInstance().disabled()) {
                Toast.makeText(getContext(), "请开启评论功能", Toast.LENGTH_SHORT).show();
                return;
            } else if (!PingLunService.getInstance().hasChangeCi()) {
                Toast.makeText(getContext(), "请选择唱段", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(getContext(), "开始评论", Toast.LENGTH_SHORT).show();
        });
    }

    private void initRecyclerView() {
        recyclerView = this.rootLayout.findViewById(R.id.zimu_detail_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ChangCiAdapter changCiAdapter = new ChangCiAdapter(PingLunService.getInstance().getChangDuan().getChangeCiList(0), getChangCiObservable());
        recyclerView.setAdapter(changCiAdapter);
    }

    private void updateTitleView(String text) {
        ((TextView) rootLayout.findViewById(R.id.currentZimuTitleTextView)).setText(text);
    }

    private ChangCiObservable getChangCiObservable() {
        ChangCiObservable changCiObservable = new ChangCiObservable();
        changCiObservable.addObserver(new ChangCiObservar());
        return changCiObservable;
    }

    class ChangCiObservable extends BaseObservable<ChangCi> {
    }

    class ChangCiListObservar implements Observer {
        @Override
        public void update(Observable o, Object arg) {
            ChangCiList.ChangCiListObservable observable = (ChangCiList.ChangCiListObservable) o;
            updateTitleView(observable.getData().getContent());
        }
    }

    class ChangCiObservar implements Observer {

        public ChangCiObservar() {

        }

        @Override
        public void update(Observable o, Object arg) {
            ChangCiObservable observable = (ChangCiObservable) o;
            updateTitleView(observable.getData().getContent());
        }
    }
}
