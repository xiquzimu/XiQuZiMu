package me.xlgp.xiquzimu.ui.tablist;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import me.xlgp.xiquzimu.R;
import me.xlgp.xiquzimu.adapter.BaseAdapter;
import me.xlgp.xiquzimu.databinding.RightItemBinding;
import me.xlgp.xiquzimu.model.ChangDuan;

public class RightAdapter extends BaseAdapter<ChangDuan> {

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(getInflatedView(R.layout.right_item, parent, false));

    }

    static class ViewHolder extends BaseAdapter.ViewHolder<ChangDuan> {
        RightItemBinding binding;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = RightItemBinding.bind(itemView);
            itemView.setOnClickListener(v -> {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(itemView, itemView, data, getAdapterPosition());
                        }
                    }
            );
        }

        @Override
        public void setData(ChangDuan data) {
            super.setData(data);
            binding.subTitle.setText(data.getJuMu());
            binding.title.setText(data.getName());
        }
    }
}
