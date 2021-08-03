package me.xlgp.douyinzimu.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.designpatterns.BaseObservable;
import me.xlgp.douyinzimu.obj.changduan.ChangCi;
import me.xlgp.douyinzimu.obj.changduan.ChangCiList;
import me.xlgp.douyinzimu.service.PingLunService;

public class ChangCiAdapter extends RecyclerView.Adapter<ChangCiAdapter.ViewHolder> {
    private ChangCiList changCiList;
    private BaseObservable<ChangCi> changCiObservable;

    public ChangCiAdapter(ChangCiList changCiList, BaseObservable<ChangCi> changCiObservable) {
        this.changCiList = changCiList;
        this.changCiObservable = changCiObservable;
    }

    @NonNull
    @Override
    public ChangCiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zimu_item_layout, parent, false);
        return new ChangCiAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChangCiAdapter.ViewHolder holder, int position) {
        holder.setData(changCiList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return changCiList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(ChangCi changCi, int position) {
            Button button = itemView.findViewById(R.id.zimu_item_btn);
            button.setText(position + 1 + ". " + changCi.getContent());
            button.setOnClickListener(v -> {
                // 设置当前唱词
                PingLunService.getInstance().getChangDuan().getChangeCiList(position);
                //重新触发评论功能
                changCiObservable.setData(changCi);
            });
        }
    }
}
