package me.xlgp.douyinzimu.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.service.ChangDuanService;

public class NameListAdapter extends BaseAdapter<String> {

    @NonNull
    @Override
    public BaseAdapter.ViewHolder<String> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fetch_item_layout, parent, false);
        return new ViewHolder(view);
    }

    protected static class ViewHolder extends BaseAdapter.ViewHolder<String> {
        private final TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Button itemBtn = itemView.findViewById(R.id.itembutton);
            textView = itemView.findViewById(R.id.textView2);
            itemBtn.setOnClickListener(v -> {
                new ChangDuanService().update(data.substring(1), Throwable::printStackTrace);
            });
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void setData(String name) {
            super.setData(name);
            textView.setText(this.getAdapterPosition() + "：" + name);
        }
    }
}
