package me.xlgp.douyinzimu.view;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.github.promeg.pinyinhelper.Pinyin;

import java.util.ArrayList;
import java.util.List;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.adapter.BaseAdapter;
import me.xlgp.douyinzimu.model.ChangDuan;

public class ChangDuanAdapter extends BaseAdapter<ChangDuan> {

    public ChangDuanAdapter(List<ChangDuan> changDuanInfoList) {
        super(changDuanInfoList);
    }

    public ChangDuanAdapter() {
        this(new ArrayList<>());
    }

    @NonNull
    @Override
    public BaseAdapter.ViewHolder<ChangDuan> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zimu_list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    protected static class ViewHolder extends BaseAdapter.ViewHolder<ChangDuan> {
        private final TextView titleView;
        private final TextView subTitleView;
        private final TextView noView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.title);
            subTitleView = itemView.findViewById(R.id.subTitle);
            noView = itemView.findViewById(R.id.no);
            //当点击唱段时，通知观察者
            itemView.setOnClickListener(v -> onItemClickListener.onItemClick(itemView, v, data, this.getAdapterPosition()));
        }

        @SuppressLint("SetTextI18n")
        public void setData(ChangDuan changDuanInfo) {
            super.setData(changDuanInfo);
            noView.setText(String.valueOf(getAdapterPosition() + 1));
            titleView.setText(changDuanInfo.getName());
            char letter = Pinyin.toPinyin(changDuanInfo.getJuMu().charAt(0)).charAt(0);
            subTitleView.setText(letter + " " + changDuanInfo.getJuZhong() + " " + changDuanInfo.getJuMu());
        }
    }
}
