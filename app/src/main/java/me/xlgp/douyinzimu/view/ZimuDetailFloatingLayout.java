package me.xlgp.douyinzimu.view;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.obj.LayoutParamsWithPoint;
import me.xlgp.douyinzimu.obj.changduan.ChangDuan;
import me.xlgp.douyinzimu.service.PingLunService;

public class ZimuDetailFloatingLayout extends BasePanelLayout {
    private final String LAYOUT_NAME = "zimu_detail_layout";
    private RecyclerView recyclerView = null;

    public ZimuDetailFloatingLayout(Context context) {
        super(context, R.layout.zimu_detail_layout);
        super.build(new LayoutParamsWithPoint(), LAYOUT_NAME);
        init();
        initRecyclerView();
    }

    private void init() {
        ChangDuan changDuan = Objects.requireNonNull(PingLunService.getInstance().getChangDuan());
        setPanelTitle(changDuan.getChangeDuanQiTa().getTitle());
    }

    private void initRecyclerView() {
        recyclerView = this.getCurrentLayout().findViewById(R.id.zimu_detail_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ChangCiAdapter changCiAdapter = new ChangCiAdapter(PingLunService.getInstance().getChangDuan().getChangeCiList());
        recyclerView.setAdapter(changCiAdapter);
    }
}
