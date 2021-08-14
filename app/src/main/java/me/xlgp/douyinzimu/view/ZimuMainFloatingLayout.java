package me.xlgp.douyinzimu.view;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.Observable;
import java.util.Observer;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.designpatterns.BaseObservable;
import me.xlgp.douyinzimu.model.ChangDuan;
import me.xlgp.douyinzimu.obj.LayoutParamsWithPoint;

public class ZimuMainFloatingLayout extends BasePanelLayout {
    private ViewPager2 viewPager2;
    private ZimuDetailFloatingLayout zimuDetailFloatingLayout;

    public ZimuMainFloatingLayout(@NonNull Context context) {
        super(context, R.layout.zimu_viewpager2_layout);
        super.build(new LayoutParamsWithPoint(new Point(getFullWidth(), 0)), this.getClass().getName());
        init();
    }

    private void init() {
        viewPager2 = getCurrentLayout().findViewById(R.id.zimu_viewpager2_layout);
        viewPager2.setAdapter(new ZimuMainFloatingAdapter());
        //懒加载，主要防止选择唱段后第二个layout还没有造成 zimuDetailFloatingLayout 为null情况
        viewPager2.setOffscreenPageLimit(2);
        setPanelTitle("字幕列表");
    }

    private class ZimuMainFloatingAdapter extends RecyclerView.Adapter<ZimuMainFloatingAdapter.ViewHolder> {

        private final static int LIST = 0;
        private final static int DETAIL = 1;


        private View inflateLayout(ViewGroup parent, int resource) {
            return LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == LIST) { //唱段列表
                return new ViewHolder(inflateLayout(parent, R.layout.zimu_floating_layout), viewType);
            } else {
                return new ViewHolder(inflateLayout(parent, R.layout.zimu_detail_layout), viewType);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull ZimuMainFloatingLayout.ZimuMainFloatingAdapter.ViewHolder holder, int position) {

        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) return LIST;
            if (position == 1) return DETAIL;
            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            return 2;
        }

        protected class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }

            public ViewHolder(@NonNull View itemView, int viewType) {
                this(itemView);
                if (viewType == LIST) {
                    new ZimuListFloatinglayout(itemView, new ChangDuanObservable());
                }
                if (viewType == DETAIL) {
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
            boolean bool = viewPager2.performAccessibilityAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD, new Bundle());
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
