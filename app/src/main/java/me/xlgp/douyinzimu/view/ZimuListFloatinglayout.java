package me.xlgp.douyinzimu.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.designpatterns.ChangDuanData;
import me.xlgp.douyinzimu.service.ChangDuanService;
import me.xlgp.douyinzimu.service.PingLunService;

public class ZimuListFloatinglayout {
    private final View rootLayout;
    private final Context context;
    RecyclerView recyclerView = null;
    private ChangDuanData changDuanData = null;
    private ZimuMainFloatingLayout.ChangDuanObservable changDuanObservable;

    public ZimuListFloatinglayout(View view, ZimuMainFloatingLayout.ChangDuanObservable changDuanObservable) {
        rootLayout = view;
        this.context = view.getContext();
        this.changDuanObservable = changDuanObservable;
        this.changDuanData = ChangDuanData.getInstance();
        initRecyclerView();
        onListener();
    }

    private void onListener() {
        this.rootLayout.findViewById(R.id.clearCurrentZiMuBtn).setOnClickListener(v -> { //清除当前唱段
            PingLunService.getInstance().clear();
            TextView textView = this.rootLayout.findViewById(R.id.currentZimuTitleTextView);
            textView.setText(R.string.currentZimuTitle);
        });

        TextView textView = this.rootLayout.findViewById(R.id.currentZimuTitleTextView);
        this.changDuanData.observe((o, arg) -> textView.setText(((ChangDuanData) o).getData().getChangDuan().getName()));
    }

    private void initRecyclerView() {
        recyclerView = this.rootLayout.findViewById(R.id.zimu_list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        ChangDuanAdapter changDuanAdapter = new ChangDuanAdapter();
        changDuanAdapter.setOnItemClickListener((itemView, data, position) -> changDuanObservable.setData(data));
        recyclerView.setAdapter(changDuanAdapter);

        ChangDuanService changDuanService = new ChangDuanService();
        changDuanService.list(list -> {
            if (list == null || list.size() == 0) {
                Toast.makeText(context, "无数据可更新", Toast.LENGTH_SHORT).show();
                return;
            }
            changDuanAdapter.updateData(list);
        });
    }
}
