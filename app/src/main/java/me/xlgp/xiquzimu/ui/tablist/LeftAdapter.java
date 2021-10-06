package me.xlgp.xiquzimu.ui.tablist;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.github.promeg.pinyinhelper.Pinyin;

import org.jetbrains.annotations.NotNull;

import me.xlgp.xiquzimu.R;
import me.xlgp.xiquzimu.adapter.BaseAdapter;
import me.xlgp.xiquzimu.databinding.LeftItemBinding;
import me.xlgp.xiquzimu.params.LeftChangDuan;

public class LeftAdapter extends BaseAdapter<LeftChangDuan> {

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        return new ViewHolder(getInflatedView(R.layout.left_item, parent, false));
    }

    static class ViewHolder extends BaseAdapter.ViewHolder<LeftChangDuan> {
        LeftItemBinding binding;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = LeftItemBinding.bind(itemView);
            itemView.setOnClickListener(v -> onItemClickListener.onItemClick(itemView, itemView, data, getAdapterPosition()));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void setData(LeftChangDuan data) {
            super.setData(data);
            char letter = '#';
            try {
                letter = Pinyin.toPinyin(data.getName().charAt(0)).charAt(0);
            } catch (Exception ignored) {
            }
            binding.textView5.setText(" " + letter + " " + data.getName());
        }
    }
}
