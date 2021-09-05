package me.xlgp.douyinzimu.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.github.promeg.pinyinhelper.Pinyin;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.model.ChangDuan;
import me.xlgp.douyinzimu.service.ChangDuanService;

public class ChangDuanListAdapter extends SearchListAdapter<ChangDuan> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.changduan_item_layout, parent, false);
        return new ViewHolder(view);
    }

    protected static class ViewHolder extends BaseAdapter.ViewHolder<ChangDuan> {
        private final TextView no;
        private final TextView content;
        private final TextView title;
        private final TextView letter;


        public ViewHolder(View view) {
            super(view);
            no = itemView.findViewById(R.id.no);
            content = itemView.findViewById(R.id.content);
            title = itemView.findViewById(R.id.title);
            letter = itemView.findViewById(R.id.letter);

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
            no.setText(this.getAdapterPosition() + "");
            title.setText(changDuan.getJuZhong() + " " + changDuan.getJuMu());
            content.setText(changDuan.getName());
            letter.setText(String.valueOf(Pinyin.toPinyin(changDuan.getJuMu().charAt(0)).charAt(0)));
        }
    }
}
