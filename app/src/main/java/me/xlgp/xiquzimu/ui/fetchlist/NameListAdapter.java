package me.xlgp.xiquzimu.ui.fetchlist;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import me.xlgp.xiquzimu.R;
import me.xlgp.xiquzimu.adapter.SearchListAdapter;
import me.xlgp.xiquzimu.databinding.FetchItemLayoutBinding;

public class NameListAdapter extends SearchListAdapter<String> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(getInflatedView(R.layout.fetch_item_layout, parent, false));
    }

    protected static class ViewHolder extends SearchListAdapter.ViewHolder<String> {
        private final FetchItemLayoutBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = FetchItemLayoutBinding.bind(itemView);
            binding.itembutton.setOnClickListener(v -> onItemClickListener.onItemClick(itemView, v, data, getAdapterPosition()));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void setData(String name) {
            super.setData(name);
            binding.textView2.setText(this.getAdapterPosition() + "：" + name);
        }
    }
}
