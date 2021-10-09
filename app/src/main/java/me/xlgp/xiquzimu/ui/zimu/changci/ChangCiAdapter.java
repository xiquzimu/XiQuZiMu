package me.xlgp.xiquzimu.ui.zimu.changci;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import me.xlgp.xiquzimu.R;
import me.xlgp.xiquzimu.adapter.BaseAdapter;
import me.xlgp.xiquzimu.databinding.ZimuDetailItemLayoutBinding;
import me.xlgp.xiquzimu.model.ChangCi;

public class ChangCiAdapter extends BaseAdapter<ChangCi> {

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