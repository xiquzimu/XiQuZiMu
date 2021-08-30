package me.xlgp.douyinzimu.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.model.ChangDuan;
import me.xlgp.douyinzimu.service.ChangDuanService;

public class ChangDuanListAdapter extends SearchListAdapter<ChangDuan> {

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
                try {
                    new ChangDuanService(new CompositeDisposable())
                            .delete(data, s -> Toast.makeText(v.getContext(), "删除成功", Toast.LENGTH_SHORT).show());
                } catch (Exception e) {
                    Toast.makeText(v.getContext(), "删除数据失败", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
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
