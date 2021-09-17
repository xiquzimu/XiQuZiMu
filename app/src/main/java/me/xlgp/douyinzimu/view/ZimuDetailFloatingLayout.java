package me.xlgp.douyinzimu.view;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import me.xlgp.douyinzimu.ZimuApplication;
import me.xlgp.douyinzimu.databinding.ZimuDetailLayoutBinding;
import me.xlgp.douyinzimu.designpatterns.ChangDuanData;
import me.xlgp.douyinzimu.listener.OnDouYinLiveListener;
import me.xlgp.douyinzimu.model.ChangCi;
import me.xlgp.douyinzimu.model.ChangDuan;
import me.xlgp.douyinzimu.obj.Callback;
import me.xlgp.douyinzimu.obj.PingLun;
import me.xlgp.douyinzimu.obj.changduan.ChangCiList;
import me.xlgp.douyinzimu.obj.changduan.ChangDuanInfo;
import me.xlgp.douyinzimu.service.ChangCiService;
import me.xlgp.douyinzimu.service.DouYinAccessibilityService;
import me.xlgp.douyinzimu.service.PingLunService;
import me.xlgp.douyinzimu.util.ChangDuanHelper;

public class ZimuDetailFloatingLayout implements OnDouYinLiveListener {

    private Context context;
    private ChangCiAdapter changCiAdapter;
    private ChangDuanData changDuanData;
    private CompositeDisposable compositeDisposable;
    private final ZimuDetailLayoutBinding binding;
    private boolean liveable;

    public ZimuDetailFloatingLayout(View view) {
        binding = ZimuDetailLayoutBinding.bind(view);
        init(view);
        onViewListener();
        initRecyclerView();
        addDouYinObserver();
    }

    private void init(View view) {
        this.context = view.getContext();
        liveable = false;
        compositeDisposable = ZimuApplication.getCompositeDisposable();
        changDuanData = ChangDuanData.getInstance();
    }

    private void addDouYinObserver(){
        DouYinAccessibilityService douYinAccessibilityService =  DouYinAccessibilityService.getInstance();
        if (douYinAccessibilityService != null){
            douYinAccessibilityService.addObserver(new DouYinObserver());
        }
    }

    private void onViewListener() {
        //选择评论
        binding.pingLunSwitchMaterial.setOnCheckedChangeListener((buttonView, isChecked) -> {
            PingLun.getInstance().change(isChecked);
            if (PingLun.getInstance().disabled()) {
                Toast.makeText(context, "评论已关闭", Toast.LENGTH_SHORT).show();
                return;
            } else if (!PingLunService.getInstance().hasChangeCi()) {
                Toast.makeText(context, "没有选择唱段", Toast.LENGTH_SHORT).show();
                return;
            }
            pingLun(PingLunService.getInstance().getChangDuanInfo().getChangeCiList().current().getDelayMillis());
            Toast.makeText(context, "开始评论", Toast.LENGTH_SHORT).show();
        });
    }

    private void pingLun(long delayMillis){
        if (liveable){
            PingLunService.getInstance().start(delayMillis);
        }
    }

    private void initRecyclerView() {
        binding.zimuDetailRecyclerview.setLayoutManager(new LinearLayoutManager(context));

        changCiAdapter = new ChangCiAdapter();
        changCiAdapter.setOnItemClickListener((itemView, view, data, position) -> {
            pingLun(0);
            updateTitleView(data.getContent());
        });

        binding.zimuDetailRecyclerview.setAdapter(changCiAdapter);
    }

    /**
     * 获取唱词并初始化唱词后是否立即评论
     */
    public void asyncRun(ChangDuan changDuan) {
        asyncGetChangDuan(changDuan, b -> {
            binding.pingLunSwitchMaterial.setChecked(false);
            if (b && liveable) { //判断是否直播界面
                binding.pingLunSwitchMaterial.setChecked(true);
            }
        });
    }

    private void updateRecyclerView(int position) {
        binding.zimuDetailRecyclerview.smoothScrollToPosition(position + 4);
    }

    private ChangCiList parseChangCiList(ChangDuan changDuan, List<ChangCi> changCis) {
        if (changCis == null || changCis.size() == 0) return new ChangCiList();
        ChangCiList changCiList = ChangDuanHelper.parseChangCiList(changDuan, changCis);
        changCiList.observe((o, arg) -> {
            ChangCi changCi = (ChangCi) arg;
            updateTitleView(changCi.getContent());
            updateRecyclerView(changCiList.currentIndex());
            if (!changCiList.hasNext()){
                binding.pingLunSwitchMaterial.setChecked(false);
            }
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
            updateRecyclerView(0);
            callback.call(true);
        }, throwable -> callback.call(false));
        compositeDisposable.add(disposable);
    }

    private void updateTitleView(String text) {
        binding.currentZimuTitleTextView.setText(text);
    }

    @Override
    public void onLive(boolean isLive) {
        liveable = isLive;
        if (!liveable){
            binding.pingLunSwitchMaterial.setChecked(false);
        }
    }

    class DouYinObserver implements Observer {
        private boolean liveable;

        public DouYinObserver(){
            liveable = false;
        }
        @Override
        public void update(Observable o, Object arg) {
            boolean liveable = (boolean) arg;
            if (liveable != this.liveable){
                this.liveable = liveable;
                ZimuDetailFloatingLayout.this.onLive(liveable);
            }
        }
    }
}
