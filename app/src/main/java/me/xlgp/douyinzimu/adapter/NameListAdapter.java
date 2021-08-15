package me.xlgp.douyinzimu.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;

import me.xlgp.douyinzimu.R;

public class NameListAdapter extends BaseAdapter<String> {

    @NonNull
    @Override
    public BaseAdapter.ViewHolder<String> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fetch_item_layout, parent, false);
        return new ViewHolder(view);
    }

    protected static class ViewHolder extends BaseAdapter.ViewHolder<String> {
        private final Button itemBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemBtn = itemView.findViewById(R.id.itembutton);
            ViewHolder holder = this;
            itemBtn.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(itemView, data, holder.getAdapterPosition());
                }
            });
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void setData(String name) {
            super.setData(name);
            itemBtn.setText(this.getAdapterPosition() + "：" + name);
        }
    }
}
