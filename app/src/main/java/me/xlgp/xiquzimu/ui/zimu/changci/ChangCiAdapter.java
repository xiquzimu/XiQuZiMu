package me.xlgp.xiquzimu.ui.zimu.changci;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.List;

import me.xlgp.xiquzimu.R;
import me.xlgp.xiquzimu.adapter.BaseAdapter;
import me.xlgp.xiquzimu.databinding.ZimuDetailItemLayoutBinding;
import me.xlgp.xiquzimu.model.ChangCi;

public class ChangCiAdapter extends BaseAdapter<ChangCi> {

    private int mHighLightPosition = 0;
    private int preHighLightPosition = -1;

    //标记当前item能否高亮，因为若不判断，当高亮行退出高亮并滑出可见区域后，再次回到该高亮行时，会再次执行高亮
    private boolean enableHightLight = false;

    private final static String REMOVE_PROGRESS = "remove_item_progress";

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(getInflatedView(R.layout.zimu_detail_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseAdapter.ViewHolder<ChangCi> holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseAdapter.ViewHolder<ChangCi> holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        try {
            ViewHolder viewHolder = ((ViewHolder) holder);
            if (!payloads.isEmpty() && payloads.contains(REMOVE_PROGRESS)) {
                viewHolder.removeProgress();
            } else if (!isHighLight(position)) {
                viewHolder.removeProgress();
            } else if (isHighLight(position)) {
                viewHolder.startProgress();
                preHighLightPosition = holder.getAdapterPosition();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hightLightItem(int position) {   // 外部调用 adapter 中这个办法，用于设置要高亮显示的位置，并调用重绘特定 position
        mHighLightPosition = position;
        enableHightLight = true;
        notifyItemChanged(preHighLightPosition, 0);
        notifyItemChanged(position, 1);
    }

    public void clearLightItem() {
        enableHightLight = false;
        notifyItemChanged(preHighLightPosition, REMOVE_PROGRESS);
        preHighLightPosition = -1;
    }

    private boolean isHighLight(int position) {  // 在 onBindViewHolder 中调用 用于判断当前是否需要高亮显示
        return mHighLightPosition == position && enableHightLight;
    }

    protected static class ViewHolder extends BaseAdapter.ViewHolder<ChangCi> {

        private final ZimuDetailItemLayoutBinding binding;
        private final Handler handler = new Handler(Looper.getMainLooper());
        private long delay = 1000;
        private final long delayMillis = 100;

        private Runnable runnable = null;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            runnable = new ProgressRunnable();
            binding = ZimuDetailItemLayoutBinding.bind(itemView);
            binding.itemLayout.setOnClickListener(v -> {
                // 设置当前唱词
                onItemClickListener.onItemClick(itemView, v, data, getAdapterPosition());
            });
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void setData(ChangCi data) {
            super.setData(data);
            binding.no.setText(String.valueOf(getAdapterPosition() + 1));
            binding.title.setText(data.getContent());
            binding.subTitle.setText(data.getShowTime() + "  " + data.getDelayMillis());
            delay = data.getDelayMillis();
            binding.progressBar.setMax((int) delay);
        }

        public void removeProgress() {
            if (binding != null) {
                binding.progressBar.setProgress(0);
            }
            handler.removeCallbacks(runnable);
        }

        public void startProgress() {
            runnable = new ProgressRunnable();
            handler.postDelayed(runnable, delayMillis);
        }

        class ProgressRunnable implements Runnable {
            private int current = 0;

            @Override
            public void run() {
                if (binding == null) {
                    removeProgress();
                    return;
                }
                binding.progressBar.setProgress(current += delayMillis);
                if (current >= delay) {
                    removeProgress();
                } else {
                    handler.postDelayed(this, delayMillis);
                }
            }
        }
    }
}
