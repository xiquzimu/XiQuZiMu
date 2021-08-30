package me.xlgp.douyinzimu.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.ZimuApplication;
import me.xlgp.douyinzimu.designpatterns.ChangDuanData;
import me.xlgp.douyinzimu.model.ChangDuan;
import me.xlgp.douyinzimu.obj.Callback;
import me.xlgp.douyinzimu.service.ChangDuanService;

public class ZimuListFloatinglayout {
    private final View rootLayout;
    private final Context context;
    private final ZimuMainFloatingLayout.ChangDuanObservable changDuanObservable;
    private final CompositeDisposable compositeDisposable;
    RecyclerView recyclerView = null;
    ChangDuanAdapter changDuanAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public ZimuListFloatinglayout(View view, ZimuMainFloatingLayout.ChangDuanObservable changDuanObservable) {
        rootLayout = view;
        this.context = view.getContext();
        this.changDuanObservable = changDuanObservable;
        this.compositeDisposable = ZimuApplication.getCompositeDisposable();

        ChangDuanData changDuanData = ChangDuanData.getInstance();
        changDuanAdapter = new ChangDuanAdapter();
        initSwipeRefreshLayout();
        initRecyclerView();

        TextView textView = this.rootLayout.findViewById(R.id.currentZimuTitleTextView);
        changDuanData.observe((o, arg) -> textView.setText(((ChangDuanData) o).getData().getChangDuan().getName()));

        swipeRefreshLayout.setRefreshing(true);
        loadData(aBoolean -> swipeRefreshLayout.setRefreshing(false));
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout = rootLayout.findViewById(R.id.zimu_list_SwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> loadData(aBoolean -> swipeRefreshLayout.setRefreshing(false)));
    }

    private void sortByPinYin(List<ChangDuan> list) {
        list.sort((o1, o2) -> {
            try {
                return Objects.requireNonNull(o1.getJuMu()).compareTo(Objects.requireNonNull(o2.getJuMu()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        });
    }

    private void loadData(Callback<Boolean> callback) {
        ChangDuanService changDuanService = new ChangDuanService(compositeDisposable);
        changDuanService.list(list -> {
            if (list == null || list.size() == 0) {
                Toast.makeText(context, "无数据可更新", Toast.LENGTH_SHORT).show();
                return;
            }
            sortByPinYin(list);
            changDuanAdapter.updateData(list);
            if (callback != null) callback.call(true);
        });
    }

    private void initRecyclerView() {
        recyclerView = this.rootLayout.findViewById(R.id.zimu_list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        changDuanAdapter.setOnItemClickListener((itemView, data, position) -> changDuanObservable.setData(data));
        recyclerView.setAdapter(changDuanAdapter);
    }
}
