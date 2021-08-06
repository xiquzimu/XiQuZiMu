package me.xlgp.douyinzimu.view;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Observable;
import java.util.Observer;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;
import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.designpatterns.BaseObservable;
import me.xlgp.douyinzimu.obj.Callback;
import me.xlgp.douyinzimu.obj.PingLun;
import me.xlgp.douyinzimu.obj.changduan.ChangCi;
import me.xlgp.douyinzimu.obj.changduan.ChangCiList;
import me.xlgp.douyinzimu.obj.changduan.ChangDuan;
import me.xlgp.douyinzimu.obj.changduan.ChangDuanInfo;
import me.xlgp.douyinzimu.service.PingLunService;
import me.xlgp.douyinzimu.util.ChangDuanHelper;

public class ZimuDetailFloatingLayout {
    private RecyclerView recyclerView = null;
    private View rootLayout;
    private Context context;
    private ChangCiAdapter changCiAdapter;
    private Button pingLunBtn;

    public ZimuDetailFloatingLayout(View view) {
        init(view);
        onViewListener();
        initRecyclerView();
    }

    private void init(View view) {
        this.rootLayout = view;
        this.context = view.getContext();
        this.pingLunBtn = this.rootLayout.findViewById(R.id.startPinglunBtn);
    }

    private void setChangCiListObservable() {
        ChangCiList.ChangCiListObservable changCiListObservable = new ChangCiList.ChangCiListObservable();
        changCiListObservable.addObserver(new ZimuDetailFloatingLayout.ChangCiListObservar());
        PingLunService.getInstance().getChangDuan().getChangeCiList().setChangCiListObservable(changCiListObservable);
    }

    private void onViewListener() {
        //开始评论
        pingLunBtn.setOnClickListener(v -> {
            if (PingLun.getInstance().disabled()) {
                Toast.makeText(context, "请开启评论功能", Toast.LENGTH_SHORT).show();
                return;
            } else if (!PingLunService.getInstance().hasChangeCi()) {
                Toast.makeText(context, "请选择唱段", Toast.LENGTH_SHORT).show();
                return;
            }
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
        asyncGetChangDuan(changDuanInfo, o -> pingLunBtn.performClick());
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
        ChangDuanHelper.getChangDuan(changDuanInfo).subscribe(new ChangDuanObserve(callback));
    }

    private void updateTitleView(String text) {
        ((TextView) rootLayout.findViewById(R.id.currentZimuTitleTextView)).setText(text);
    }

    private ChangCiObservable getChangCiObservable() {
        ChangCiObservable changCiObservable = new ChangCiObservable();
        changCiObservable.addObserver(new ChangCiObservar());
        return changCiObservable;
    }

    class ChangDuanObserve implements io.reactivex.rxjava3.core.Observer<ChangDuan> {
        private final Callback<Object> callback;

        public ChangDuanObserve(Callback<Object> callback) {
            this.callback = callback;
        }

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull ChangDuan changDuan) {
            PingLunService.getInstance().setChangDuan(changDuan);
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

    static class ChangCiObservable extends BaseObservable<ChangCi> {
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
