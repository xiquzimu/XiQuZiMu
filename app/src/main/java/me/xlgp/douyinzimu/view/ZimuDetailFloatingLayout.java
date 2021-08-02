package me.xlgp.douyinzimu.view;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.obj.LayoutParamsWithPoint;
import me.xlgp.douyinzimu.service.PingLunService;

public class ZimuDetailFloatingLayout extends BasePenalLayout {
    private final String LAYOUT_NAME = "zimu_detail_layout";
    private View rootLayout;
    private RecyclerView recyclerView = null;

    public ZimuDetailFloatingLayout(Context context) {
        super(context, R.layout.zimu_detail_layout);
        super.build(new LayoutParamsWithPoint(), LAYOUT_NAME);
        this.rootLayout = getCurrentLayout();
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView = this.rootLayout.findViewById(R.id.zimu_detail_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ChangCiAdapter changCiAdapter = new ChangCiAdapter(PingLunService.getInstance().getChangeCiList());
        recyclerView.setAdapter(changCiAdapter);
    }
}
