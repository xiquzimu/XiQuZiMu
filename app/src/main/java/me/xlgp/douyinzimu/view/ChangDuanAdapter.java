package me.xlgp.douyinzimu.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.obj.changduan.ChangDuanInfo;

public class ChangDuanAdapter extends RecyclerView.Adapter<ChangDuanAdapter.ViewHolder> {
    private List<ChangDuanInfo> changDuanInfoList;
    private ZimuListFloatinglayout.ChangDuanObservable changDuanObservable;

    public ChangDuanAdapter(List<ChangDuanInfo> changDuanInfoList, ZimuListFloatinglayout.ChangDuanObservable observable) {
        this.changDuanInfoList = changDuanInfoList;
        this.changDuanObservable = observable;
    }

    public ChangDuanAdapter() {
        this(new ArrayList<>(), null);
    }

    public ChangDuanAdapter(ZimuListFloatinglayout.ChangDuanObservable observable) {
        this(new ArrayList<>(), observable);
    }

    public void updateData(List<ChangDuanInfo> list) {
        changDuanInfoList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChangDuanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zimu_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChangDuanAdapter.ViewHolder holder, int position) {
        ChangDuanInfo changDuanInfo = changDuanInfoList.get(position);
        holder.setData(changDuanInfo, position);
    }

    @Override
    public int getItemCount() {
        return changDuanInfoList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private Button button;
        private ChangDuanInfo changDuanInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.zimu_item_btn);
            //当点击唱段时，通知观察者
            button.setOnClickListener(v -> changDuanObservable.setData(changDuanInfo));
        }

        public void setData(ChangDuanInfo changDuanInfo, int position) {
            button.setText(position + 1 + ". " + changDuanInfo.getName());
            this.changDuanInfo = changDuanInfo;
        }
    }
}
