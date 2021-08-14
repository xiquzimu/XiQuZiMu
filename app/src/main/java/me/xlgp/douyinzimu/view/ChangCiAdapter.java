package me.xlgp.douyinzimu.view;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.designpatterns.BaseObservable;
import me.xlgp.douyinzimu.designpatterns.ChangDuanData;
import me.xlgp.douyinzimu.model.ChangCi;
import me.xlgp.douyinzimu.obj.changduan.ChangCiList;
import me.xlgp.douyinzimu.service.PingLunService;

public class ChangCiAdapter extends RecyclerView.Adapter<ChangCiAdapter.ViewHolder> {

    private ChangCiList changCiList;
    private ChangDuanData changDuanData;
    private BaseObservable<ChangCi> changCiBaseObservable;

    public ChangCiAdapter(ChangCiList changCiList, BaseObservable<ChangCi> changCiBaseObservable) {
        this.changCiList = changCiList;
        this.changCiBaseObservable = changCiBaseObservable;
        this.changDuanData = ChangDuanData.getInstance();
        this.changDuanData.observe((o, arg) -> updateData(((ChangDuanData) o).getData().getChangeCiList()));
    }

    public ChangCiAdapter(BaseObservable<ChangCi> changCiBaseObservable) {
        this(new ChangCiList(), changCiBaseObservable);
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

    public void updateData(ChangCiList changCiList) {
        this.changCiList = changCiList;
        notifyDataSetChanged();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        Button button = itemView.findViewById(R.id.zimu_item_btn);

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @SuppressLint("SetTextI18n")
        public void setData(me.xlgp.douyinzimu.model.ChangCi changCi, int position) {

            button.setText((position + 1) + ". " + changCi.getContent());
            button.setOnClickListener(v -> {
                // 设置当前唱词
                PingLunService.getInstance().getChangDuanInfo().getChangeCiList(position);
                changCiBaseObservable.setData(changCi);
            });
        }
    }
}
