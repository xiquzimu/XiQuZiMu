package me.xlgp.douyinzimu.view;

import android.content.Context;
import android.view.View;
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
    private ChangDuanInfo changDuanInfo;
    private ChangCiAdapter changCiAdapter;

    public ZimuDetailFloatingLayout(View view) {
        this(view, null);
    }

    public ZimuDetailFloatingLayout(View view, ChangDuanInfo changDuanInfo) {
        init(view, changDuanInfo);
        onViewListener();
        initRecyclerView();
    }

    private void init(View view, ChangDuanInfo changDuanInfo) {
        this.changDuanInfo = changDuanInfo;
        this.rootLayout = view;
        this.context = view.getContext();
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

        asyncGetChangDuan(changDuanInfo);
    }

    public void asyncGetChangDuan(ChangDuanInfo changDuanInfo) {
        //异步获取唱词
        if (changCiAdapter == null) {
            Toast.makeText(context, "唱词列表初始化异常", Toast.LENGTH_SHORT).show();
            return;
        }
        if (changDuanInfo == null) {
            Toast.makeText(context, "请选择唱段", Toast.LENGTH_SHORT).show();
            return;
        }
        ChangDuanHelper.getChangDuan(changDuanInfo).subscribe(new io.reactivex.rxjava3.core.Observer<ChangDuan>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull ChangDuan changDuan) {
                PingLunService.getInstance().setChangDuan(changDuan);
                changCiAdapter.updateData(changDuan.getChangeCiList(0));
                setChangCiListObservable();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(context, "加载唱词失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

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
