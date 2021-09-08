package me.xlgp.douyinzimu.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.ZimuApplication;
import me.xlgp.douyinzimu.designpatterns.ChangDuanData;
import me.xlgp.douyinzimu.model.ChangCi;
import me.xlgp.douyinzimu.model.ChangDuan;
import me.xlgp.douyinzimu.obj.Callback;
import me.xlgp.douyinzimu.obj.PingLun;
import me.xlgp.douyinzimu.obj.changduan.ChangCiList;
import me.xlgp.douyinzimu.obj.changduan.ChangDuanInfo;
import me.xlgp.douyinzimu.service.ChangCiService;
import me.xlgp.douyinzimu.service.PingLunService;
import me.xlgp.douyinzimu.util.ChangDuanHelper;

public class ZimuDetailFloatingLayout {
    private RecyclerView recyclerView = null;
    private View rootLayout;
    private Context context;
    private ChangCiAdapter changCiAdapter;
    private SwitchMaterial switchMaterial;
    private ChangDuanData changDuanData;
    private CompositeDisposable compositeDisposable;

    public ZimuDetailFloatingLayout(View view) {
        init(view);
        onViewListener();
        initRecyclerView();
    }

    private void init(View view) {
        this.rootLayout = view;
        this.context = view.getContext();
        compositeDisposable = ZimuApplication.getCompositeDisposable();
        changDuanData = ChangDuanData.getInstance();
        this.switchMaterial = this.rootLayout.findViewById(R.id.pingLunSwitchMaterial);
    }

    private void onViewListener() {
        //选择评论
        switchMaterial.setOnCheckedChangeListener((buttonView, isChecked) -> {
            PingLun.getInstance().change(isChecked);
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

        changCiAdapter = new ChangCiAdapter();
        changCiAdapter.setOnItemClickListener((itemView, view, data, position) -> {
            PingLunService.getInstance().start(0);
            updateTitleView(data.getContent());
        });

        recyclerView.setAdapter(changCiAdapter);
    }

    /**
     * 获取唱词并初始化唱词后是否立即评论
     */
    public void asyncRun(ChangDuan changDuan) {
        asyncGetChangDuan(changDuan, b -> {
            switchMaterial.setChecked(false);
            if (b) {
                switchMaterial.setChecked(true);
            }
        });
    }

    private void updateRecyclerView(int position) {
        recyclerView.smoothScrollToPosition(position + 4);
    }

    private ChangCiList parseChangCiList(ChangDuan changDuan, List<ChangCi> changCis) {
        if (changCis == null || changCis.size() == 0) return new ChangCiList();
        ChangCiList changCiList = ChangDuanHelper.parseChangCiList(changDuan, changCis);
        changCiList.observe((o, arg) -> {
            ChangCi changCi = (ChangCi) arg;
            updateTitleView(changCi.getContent());
            updateRecyclerView(changCiList.currentIndex());
        });
        return changCiList;
    }

    public void asyncGetChangDuan(ChangDuan changDuan, Callback<Boolean> callback) {
        //异步获取唱词
        if (changCiAdapter == null) {
            Toast.makeText(context, "唱词列表初始化异常", Toast.LENGTH_SHORT).show();
            return;
        }
        if (changDuan == null) {
            Toast.makeText(context, "请选择唱段", Toast.LENGTH_SHORT).show();
            return;
        }

        ChangCiService changCiService = new ChangCiService();
        Disposable disposable = changCiService.listByChangDuanId(changDuan.getId()).subscribe(list -> {
            ChangDuanInfo changDuanInfo = new ChangDuanInfo();
            changDuanInfo.setChangCiList(parseChangCiList(changDuan, list));
            changDuanInfo.setChangDuan(changDuan);

            changDuanData.setData(changDuanInfo);
            callback.call(true);
        }, throwable -> callback.call(false));
        compositeDisposable.add(disposable);
    }

    private void updateTitleView(String text) {
        ((TextView) rootLayout.findViewById(R.id.currentZimuTitleTextView)).setText(text);
    }
}
