package me.xlgp.xiquzimu.ui.zimu.changduan;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.github.promeg.pinyinhelper.Pinyin;

import java.util.ArrayList;
import java.util.List;

import me.xlgp.xiquzimu.R;
import me.xlgp.xiquzimu.adapter.BaseAdapter;
import me.xlgp.xiquzimu.databinding.ZimuListItemLayoutBinding;
import me.xlgp.xiquzimu.model.ChangDuan;

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
        return new ViewHolder(getInflatedView(R.layout.zimu_list_item_layout, parent, false));
    }

    protected static class ViewHolder extends BaseAdapter.ViewHolder<ChangDuan> {
        private final ZimuListItemLayoutBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ZimuListItemLayoutBinding.bind(itemView);
            //当点击唱段时，通知观察者
            itemView.setOnClickListener(v -> onItemClickListener.onItemClick(itemView, v, data, this.getAdapterPosition()));
        }

        @SuppressLint("SetTextI18n")
        public void setData(ChangDuan changDuanInfo) {
            super.setData(changDuanInfo);
            binding.title.setText(changDuanInfo.getName());
            char letter = Pinyin.toPinyin(changDuanInfo.getJuMu().charAt(0)).charAt(0);
            binding.letter.setText(String.valueOf(letter));
            binding.subTitle.setText(changDuanInfo.getJuZhong() + " " + changDuanInfo.getJuMu());
        }
    }
}
