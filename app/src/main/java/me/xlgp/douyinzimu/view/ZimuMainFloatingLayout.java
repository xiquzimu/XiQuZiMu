package me.xlgp.douyinzimu.view;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        super.build(new LayoutParamsWithPoint(new Point(getFullWidth(),0)), this.getClass().getName());
        init();
    }

    private void init() {
        viewPager2 = (ViewPager2) getCurrentLayout();
        viewPager2.setAdapter(new ZimuMainFloatingAdapter());
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
            if (viewType == LIST) {
                View view = inflateLayout(parent, R.layout.zimu_floating_layout);
                return new ViewHolder(view, viewType);
            }
            if (viewType == DETAIL) {
                return new ViewHolder(inflateLayout(parent, R.layout.zimu_detail_layout), viewType);
            }
            return new ViewHolder(null, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull ZimuMainFloatingLayout.ZimuMainFloatingAdapter.ViewHolder holder, int position) {
            Log.i(TAG, "onBindViewHolder: " + position);
            holder.setData(holder.getItemViewType() + " - " + position, position);
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
            private int viewType;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }

            public ViewHolder(@NonNull View itemView, int viewType){
                this(itemView);
                this.viewType = viewType;
            }

            public void setData(String string, int position) {

                if (viewType == LIST) {
                    new ZimuListFloatinglayout(itemView);
                    TextView textView = itemView.findViewById(R.id.currentZimuTitleTextView);
                    textView.setText(string);
                }
                if (viewType == DETAIL) {
                    Log.i(TAG, "setData: " + string);
                }
            }
        }
    }
}
