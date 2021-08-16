package me.xlgp.douyinzimu.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import io.reactivex.rxjava3.functions.Consumer;
import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.model.ChangDuan;
import me.xlgp.douyinzimu.service.ChangDuanService;

public class ChangDuanListAdapter extends BaseAdapter<ChangDuan> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fetch_item_layout, parent, false);
        return new ViewHolder(view);
    }

    protected static class ViewHolder extends BaseAdapter.ViewHolder<ChangDuan> {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = itemView.findViewById(R.id.textView2);
            view.setOnLongClickListener(v -> {
                new ChangDuanService().delete(data, s -> Log.i("delete item", "accept: "));
                return false;
            });
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void setData(ChangDuan changDuan) {
            super.setData(changDuan);
            textView.setText(this.getAdapterPosition() + "：" + changDuan.getName());
        }
    }
}
