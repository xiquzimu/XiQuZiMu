package me.xlgp.douyinzimu.view;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.adapter.BaseAdapter;
import me.xlgp.douyinzimu.designpatterns.ChangDuanData;
import me.xlgp.douyinzimu.model.ChangCi;

public class ChangCiAdapter extends BaseAdapter<ChangCi> {

    public ChangCiAdapter() {
        ChangDuanData changDuanData = ChangDuanData.getInstance();
        changDuanData.observe((o, arg) -> updateData(((ChangDuanData) o).getData().getChangeCiList()));
    }

    @NonNull
    @Override
    public BaseAdapter.ViewHolder<ChangCi> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zimu_detail_item_layout, parent, false);
        return new ViewHolder(view);
    }

    protected static class ViewHolder extends BaseAdapter.ViewHolder<ChangCi> {

        TextView no = itemView.findViewById(R.id.no);
        TextView title = itemView.findViewById(R.id.title);
        TextView subTitle = itemView.findViewById(R.id.subTitle);

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> {
                // 设置当前唱词
                ChangDuanData.getInstance().getData().getChangeCiList(getAdapterPosition());
                onItemClickListener.onItemClick(itemView, v, data, getAdapterPosition());
            });
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void setData(ChangCi data) {
            super.setData(data);
            no.setText(String.valueOf(getAdapterPosition() + 1));
            title.setText(data.getContent());
            subTitle.setText(data.getShowTime() + "  " + data.getDelayMillis());
        }
    }
}
