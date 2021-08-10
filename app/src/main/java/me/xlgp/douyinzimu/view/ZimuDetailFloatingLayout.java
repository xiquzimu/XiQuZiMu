package me.xlgp.douyinzimu.view;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.dao.ChangCiDao;
import me.xlgp.douyinzimu.db.AppDatabase;
import me.xlgp.douyinzimu.designpatterns.BaseObservable;
import me.xlgp.douyinzimu.model.ChangCi;
import me.xlgp.douyinzimu.obj.Callback;
import me.xlgp.douyinzimu.obj.PingLun;
import me.xlgp.douyinzimu.obj.changduan.ChangCiList;
import me.xlgp.douyinzimu.obj.changduan.ChangDuanInfo;
import me.xlgp.douyinzimu.service.PingLunService;

public class ZimuDetailFloatingLayout {
    private RecyclerView recyclerView = null;
    private View rootLayout;
    private Context context;
    private ChangCiAdapter changCiAdapter;
    private SwitchMaterial switchMaterial;

    public ZimuDetailFloatingLayout(View view) {
        init(view);
        onViewListener();
        initRecyclerView();
    }

    private void init(View view) {
        this.rootLayout = view;
        this.context = view.getContext();
        this.switchMaterial = this.rootLayout.findViewById(R.id.pingLunSwitchMaterial);
    }

    private void setChangCiListObservable() {
        ChangCiList.ChangCiListObservable changCiListObservable = new ChangCiList.ChangCiListObservable();
        changCiListObservable.addObserver(new ZimuDetailFloatingLayout.ChangCiListObservar());
        PingLunService.getInstance().getChangDuanInfo().getChangeCiList().setChangCiListObservable(changCiListObservable);
    }

    private void onViewListener() {
        //选择评论
        switchMaterial.setOnCheckedChangeListener((buttonView, isChecked) -> {
            PingLun.getInstance().change(isChecked);
            Log.i("switchMaterial", "onViewListener: " + System.currentTimeMillis());
            PingLunService pingLunService = PingLunService.getInstance();
            if (PingLun.getInstance().disabled()) {
                Toast.makeText(context, "评论已关闭", Toast.LENGTH_SHORT).show();
                return;
            } else if (!PingLunService.getInstance().hasChangeCi()) {
                Toast.makeText(context, "没有选择唱段", Toast.LENGTH_SHORT).show();
                return;
            }
            pingLunService.start(pingLunService.getChangDuanInfo().getChangeCiList().current().getDelayMillis());
            Toast.makeText(context, "开始评论", Toast.LENGTH_SHORT).show();
        });
    }

    private void initRecyclerView() {
        recyclerView = this.rootLayout.findViewById(R.id.zimu_detail_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        changCiAdapter = new ChangCiAdapter(getChangCiObservable());
        recyclerView.setAdapter(changCiAdapter);
    }

    /**
     * 获取唱词并初始化唱词后是否立即评论
     */
    public void asyncRun(ChangDuanInfo changDuanInfo) {
        asyncGetChangDuan(changDuanInfo, o -> {
            switchMaterial.setChecked(false);
            switchMaterial.setChecked(true);
        });
    }

    public void asyncGetChangDuan(ChangDuanInfo changDuanInfo, Callback<Object> callback) {
        //异步获取唱词
        if (changCiAdapter == null) {
            Toast.makeText(context, "唱词列表初始化异常", Toast.LENGTH_SHORT).show();
            return;
        }
        if (changDuanInfo == null) {
            Toast.makeText(context, "请选择唱段", Toast.LENGTH_SHORT).show();
            return;
        }

        ChangCiDao changCiDao = AppDatabase.getInstance(context).changCiDao();
        changCiDao.listByChangDuanId(changDuanInfo.getChangDuan().getId()).subscribe(new Consumer<List<me.xlgp.douyinzimu.model.ChangCi>>() {
            @Override
            public void accept(List<me.xlgp.douyinzimu.model.ChangCi> changCis) throws Throwable {
                PingLunService.getInstance().setChangDuanInfo(changDuanInfo);
                ChangCiList changCiList = new ChangCiList();
                for (int i = 0; i < changCis.size(); i++) {
                    changCiList.add(changCis.get(i));
                }
                changCiAdapter.updateData(changCiList);
                setChangCiListObservable();
            }
        });
    }

    private void updateTitleView(String text) {
        ((TextView) rootLayout.findViewById(R.id.currentZimuTitleTextView)).setText(text);
    }

    private ChangCiObservable getChangCiObservable() {
        ChangCiObservable changCiObservable = new ChangCiObservable();
        changCiObservable.addObserver(new ChangCiObservar());
        return changCiObservable;
    }

    static class ChangCiObservable extends BaseObservable<ChangCi> {
    }

    class ChangDuanObserve implements io.reactivex.rxjava3.core.Observer<ChangDuanInfo> {
        private final Callback<Object> callback;

        public ChangDuanObserve(Callback<Object> callback) {
            this.callback = callback;
        }

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull ChangDuanInfo changDuan) {
            PingLunService.getInstance().setChangDuanInfo(changDuan);
            changCiAdapter.updateData(changDuan.getChangeCiList(0));
            setChangCiListObservable();
            if (callback != null) callback.call(null);
        }

        @Override
        public void onError(@NonNull Throwable e) {

        }

        @Override
        public void onComplete() {

        }
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
            //点击当前唱词应立即执行
            PingLunService.getInstance().start(0);
            updateTitleView(observable.getData().getContent());
        }
    }
}
