package me.xlgp.douyinzimu.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Observable;
import java.util.Observer;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.dao.ChangDuanDao;
import me.xlgp.douyinzimu.db.AppDatabase;
import me.xlgp.douyinzimu.designpatterns.BaseObservable;
import me.xlgp.douyinzimu.model.ChangDuan;
import me.xlgp.douyinzimu.obj.changduan.ChangDuanInfo;
import me.xlgp.douyinzimu.service.PingLunService;

public class ZimuListFloatinglayout {
    private final View rootLayout;
    private final Context context;
    RecyclerView recyclerView = null;
    private ZimuMainFloatingLayout.ChangDuanObservable changDuanObservable;

    public ZimuListFloatinglayout(View view, ZimuMainFloatingLayout.ChangDuanObservable changDuanObservable) {
        rootLayout = view;
        this.context = view.getContext();
        this.changDuanObservable = changDuanObservable;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        ChangDuanAdapter changDuanAdapter = new ChangDuanAdapter(getChangDuanObservable());
        recyclerView.setAdapter(changDuanAdapter);

        ChangDuanDao changDuanDao = AppDatabase.getInstance().changDuanDao();
        changDuanDao.list().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(list -> {
            if (list == null || list.size() == 0) {
                Toast.makeText(context, "无数据可更新", Toast.LENGTH_SHORT).show();
                return;
            }
            changDuanAdapter.updateData(list);
        });
    }

    private ChangDuanObservable getChangDuanObservable() {
        ChangDuanObservable observable = new ChangDuanObservable();
        observable.addObserver(new CurrentZimuItemObserver(this.rootLayout.findViewById(R.id.currentZimuTitleTextView)));
        observable.addObserver(new ChangeCiListObserver(changDuanObservable));
        return observable;
    }

    /**
     * 当先选中唱段观察者，
     */
    private static class ChangeCiListObserver implements Observer {
        ZimuMainFloatingLayout.ChangDuanObservable changDuanObservable;

        private ChangeCiListObserver(ZimuMainFloatingLayout.ChangDuanObservable changDuanObservable) {
            this.changDuanObservable = changDuanObservable;
        }

        @Override
        public void update(Observable o, Object arg) {
            ChangDuanObservable observable = (ChangDuanObservable) o;
            ChangDuanInfo changDuanInfo = new ChangDuanInfo();
            changDuanInfo.setChangDuan(observable.getData());
            changDuanObservable.setData(changDuanInfo);
        }
    }

    static class ChangDuanObservable extends BaseObservable<ChangDuan> {
    }

    /**
     * 当前选中的唱段观察者
     */
    private static class CurrentZimuItemObserver implements Observer {
        private final TextView textView;

        public CurrentZimuItemObserver(TextView textView) {
            this.textView = textView;
        }

        @Override
        public void update(Observable o, Object arg) {
            ChangDuanObservable observable = (ChangDuanObservable) o;

            ChangDuanInfo changDuanInfo = new ChangDuanInfo();
            changDuanInfo.setChangDuan(observable.getData());
            PingLunService.getInstance().setChangDuanInfo(changDuanInfo);
            textView.setText(observable.getData().getName());
        }
    }
}
