package me.xlgp.douyinzimu.ui.zimu.changci;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.adapter.BaseAdapter;
import me.xlgp.douyinzimu.databinding.ZimuDetailItemLayoutBinding;
import me.xlgp.douyinzimu.model.ChangCi;

public class ChangCiAdapter extends BaseAdapter<ChangCi> {

    private int position = 0;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @NonNull
    @Override
    public BaseAdapter.ViewHolder<ChangCi> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(getInflatedView(R.layout.zimu_detail_item_layout, parent, false));
    }

    protected static class ViewHolder extends BaseAdapter.ViewHolder<ChangCi> {

        private final ZimuDetailItemLayoutBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ZimuDetailItemLayoutBinding.bind(itemView);
            itemView.setOnClickListener(v -> {
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
        }
    }
}
