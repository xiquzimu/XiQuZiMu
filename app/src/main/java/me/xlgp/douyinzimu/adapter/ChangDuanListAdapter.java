package me.xlgp.douyinzimu.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.github.promeg.pinyinhelper.Pinyin;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.databinding.ChangduanItemLayoutBinding;
import me.xlgp.douyinzimu.model.ChangDuan;
import me.xlgp.douyinzimu.data.ChangDuanRepository;

public class ChangDuanListAdapter extends SearchListAdapter<ChangDuan> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(getInflatedView(R.layout.changduan_item_layout, parent, false));
    }

    protected static class ViewHolder extends BaseAdapter.ViewHolder<ChangDuan> {
        private final ChangduanItemLayoutBinding binding;

        public ViewHolder(View view) {
            super(view);
            binding = ChangduanItemLayoutBinding.bind(view);

            view.setOnLongClickListener(v -> {
                try {
                    new ChangDuanRepository(new CompositeDisposable())
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
            binding.no.setText(this.getAdapterPosition() + "");
            binding.title.setText(changDuan.getJuZhong() + " " + changDuan.getJuMu());
            binding.content.setText(changDuan.getName());
            binding.letter.setText(String.valueOf(Pinyin.toPinyin(changDuan.getJuMu().charAt(0)).charAt(0)));
        }
    }
}
