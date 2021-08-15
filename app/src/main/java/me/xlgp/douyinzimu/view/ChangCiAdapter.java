package me.xlgp.douyinzimu.view;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.adapter.BaseAdapter;
import me.xlgp.douyinzimu.designpatterns.ChangDuanData;
import me.xlgp.douyinzimu.model.ChangCi;
import me.xlgp.douyinzimu.service.PingLunService;

public class ChangCiAdapter extends BaseAdapter<ChangCi> {

    public ChangCiAdapter() {
        ChangDuanData changDuanData = ChangDuanData.getInstance();
        changDuanData.observe((o, arg) -> updateData(((ChangDuanData) o).getData().getChangeCiList()));
    }

    @NonNull
    @Override
    public BaseAdapter.ViewHolder<ChangCi> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zimu_item_layout, parent, false);
        return new ViewHolder(view);
    }

    protected static class ViewHolder extends BaseAdapter.ViewHolder<ChangCi> {

        Button button = itemView.findViewById(R.id.zimu_item_btn);

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button.setOnClickListener(v -> {
                // 设置当前唱词
                PingLunService.getInstance().getChangDuanInfo().getChangeCiList(getAdapterPosition());
                onItemClickListener.onItemClick(itemView, data, getAdapterPosition());
            });
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void setData(ChangCi data) {
            super.setData(data);
            button.setText((getAdapterPosition() + 1) + ". " + data.getContent());
        }
    }
}
