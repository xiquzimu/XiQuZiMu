package me.xlgp.douyinzimu.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.obj.LayoutParamsWithPoint;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class ZimuMainFloatingLayout extends BasePanelLayout {
    private ViewPager2 viewPager2;

    public ZimuMainFloatingLayout(@NonNull Context context) {
        super(context, R.layout.zimu_viewpager2_layout);
        super.build(new LayoutParamsWithPoint(), this.getClass().getName());
        init();
    }

    private void init() {
        viewPager2 = (ViewPager2) getCurrentLayout();
        viewPager2.setAdapter(new ZimuMainFloatingAdapter());
    }

    private class ZimuMainFloatingAdapter extends RecyclerView.Adapter<ZimuMainFloatingAdapter.ViewHolder> {

        private final static int TOOL = 0;
        private final static int DETAIL = 1;


        private View inflateLayout(ViewGroup parent, int resource) {
            return LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == TOOL) {
                return new ViewHolder(inflateLayout(parent, R.layout.zimu_detail_layout));
            }
            if (viewType == DETAIL) {
                return new ViewHolder(inflateLayout(parent, R.layout.zimu_floating_layout));
            }
            return new ViewHolder(null);
        }

        @Override
        public void onBindViewHolder(@NonNull ZimuMainFloatingLayout.ZimuMainFloatingAdapter.ViewHolder holder, int position) {
            Log.i(TAG, "onBindViewHolder: " + position);
            holder.setData(holder.getItemViewType() + " - " + position);
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) return TOOL;
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

            public void setData(String string) {
                Log.i(TAG, "setData: " + string);
            }
        }
    }
}
