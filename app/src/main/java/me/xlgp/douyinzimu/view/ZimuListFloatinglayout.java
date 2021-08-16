package me.xlgp.douyinzimu.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.designpatterns.ChangDuanData;
import me.xlgp.douyinzimu.obj.Callback;
import me.xlgp.douyinzimu.service.ChangDuanService;

public class ZimuListFloatinglayout {
    private final View rootLayout;
    private final Context context;
    RecyclerView recyclerView = null;
    ChangDuanAdapter changDuanAdapter = null;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ChangDuanData changDuanData = null;
    private ZimuMainFloatingLayout.ChangDuanObservable changDuanObservable;

    public ZimuListFloatinglayout(View view, ZimuMainFloatingLayout.ChangDuanObservable changDuanObservable) {
        rootLayout = view;
        this.context = view.getContext();
        this.changDuanObservable = changDuanObservable;
        this.changDuanData = ChangDuanData.getInstance();
        changDuanAdapter = new ChangDuanAdapter();
        initSwipeRefreshLayout();
        initRecyclerView();

        TextView textView = this.rootLayout.findViewById(R.id.currentZimuTitleTextView);
        this.changDuanData.observe((o, arg) -> textView.setText(((ChangDuanData) o).getData().getChangDuan().getName()));
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout = rootLayout.findViewById(R.id.zimu_list_SwipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(() -> loadData(aBoolean -> swipeRefreshLayout.setRefreshing(false)));
    }

    private void loadData(Callback<Boolean> callback) {
        ChangDuanService changDuanService = new ChangDuanService();
        changDuanService.list(list -> {
            if (callback != null) callback.call(true);
            if (list == null || list.size() == 0) {
                Toast.makeText(context, "无数据可更新", Toast.LENGTH_SHORT).show();
                return;
            }
            changDuanAdapter.updateData(list);
        });
    }

    private void initRecyclerView() {
        recyclerView = this.rootLayout.findViewById(R.id.zimu_list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        changDuanAdapter.setOnItemClickListener((itemView, data, position) -> changDuanObservable.setData(data));
        recyclerView.setAdapter(changDuanAdapter);
        loadData(null);
    }
}
