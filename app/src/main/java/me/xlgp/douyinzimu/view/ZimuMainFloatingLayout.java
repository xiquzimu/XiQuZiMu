package me.xlgp.douyinzimu.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Observable;
import java.util.Observer;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.databinding.ZimuViewpager2LayoutBinding;
import me.xlgp.douyinzimu.designpatterns.BaseObservable;
import me.xlgp.douyinzimu.listener.OnDoubleClickListener;
import me.xlgp.douyinzimu.model.ChangDuan;
import me.xlgp.douyinzimu.obj.ZimuLayoutParams;
import me.xlgp.douyinzimu.service.DianZanService;
import me.xlgp.douyinzimu.service.FloatingService;

public class ZimuMainFloatingLayout extends BasePanelLayout {

    private ZimuDetailFloatingLayout zimuDetailFloatingLayout;
    private final ZimuViewpager2LayoutBinding binding;
    private final FloatingService floatingService;

    public ZimuMainFloatingLayout(@NonNull FloatingService floatingService) {
        super(floatingService, R.layout.zimu_viewpager2_layout);
        this.floatingService = floatingService;

        super.build(new ZimuLayoutParams.WithFullWidth(), this.getClass().getName());

        this.binding = ZimuViewpager2LayoutBinding.bind(getCurrentLayout());

        init();
    }

    @Override
    public void onClose() {
        super.onClose();
        floatingService.stop();
    }

    private void init() {

        binding.zimuViewpager2.setAdapter(new ZimuMainFloatingAdapter());
        //懒加载，主要防止选择唱段后第二个layout还没有造成 zimuDetailFloatingLayout 为null情况
        binding.zimuViewpager2.setOffscreenPageLimit(2);

        setPanelTitle("字幕列表");

        setOnTitleClickListener(new OnDoubleClickListener() {
            @Override
            public void doubleClick(View v) {
                new DianZanService().dianZan();
            }
        });

        initTabList();
    }

    private void initTabList() {
        String[] names = new String[]{"黄梅戏", "唱词"};
        for (String name : names) {
            TabLayout.Tab tab = binding.zimuTabList.newTab();
            tab.setText(name);
            binding.zimuTabList.addTab(tab);
        }
        new TabLayoutMediator(binding.zimuTabList, binding.zimuViewpager2, (tab, position) -> tab.setText(names[position])).attach();
    }

    private class ZimuMainFloatingAdapter extends RecyclerView.Adapter<ZimuMainFloatingAdapter.ViewHolder> {

        private final int[] viewIdList = new int[]{R.layout.zimu_floating_layout, R.layout.zimu_detail_layout};

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            return new ViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull ZimuMainFloatingLayout.ZimuMainFloatingAdapter.ViewHolder holder, int position) {

        }

        @Override
        public int getItemViewType(int position) {
            return viewIdList[position];
        }

        @Override
        public int getItemCount() {
            return viewIdList.length;
        }

        protected class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }

            public ViewHolder(@NonNull View itemView, int viewType) {
                this(itemView);
                if (viewType == viewIdList[0]) {
                    new ZimuListFloatinglayout(itemView, new ChangDuanObservable());
                }
                if (viewType == viewIdList[1]) {
                    zimuDetailFloatingLayout = new ZimuDetailFloatingLayout(itemView);
                }
            }
        }
    }

    class ChangDuanObservable extends BaseObservable<ChangDuan> {
        public ChangDuanObservable() {
            this.addObserver(new ChangeDuanObserver());
        }
    }

    private class ChangeDuanObserver implements Observer {
        @Override
        public void update(Observable o, Object arg) {
            //滑动切换到唱词列表
            boolean bool = binding.zimuViewpager2.performAccessibilityAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD,
                    null);
            if (bool) {
                ChangDuanObservable changDuanObservable = (ChangDuanObservable) o;
                //切换成功后
                if (zimuDetailFloatingLayout != null) {
                    zimuDetailFloatingLayout.asyncRun(changDuanObservable.getData());
                }
            }
        }
    }
}
