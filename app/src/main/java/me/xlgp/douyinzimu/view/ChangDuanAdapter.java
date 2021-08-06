package me.xlgp.douyinzimu.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.obj.changduan.ChangDuanInfo;

public class ChangDuanAdapter extends RecyclerView.Adapter<ChangDuanAdapter.ViewHolder> {
    private final ZimuListFloatinglayout.ChangDuanObservable changDuanObservable;
    private List<ChangDuanInfo> changDuanInfoList;

    public ChangDuanAdapter(List<ChangDuanInfo> changDuanInfoList, ZimuListFloatinglayout.ChangDuanObservable observable) {
        this.changDuanInfoList = changDuanInfoList;
        this.changDuanObservable = observable;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zimu_list_item_layout, parent, false);
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
        private final TextView titleView;
        private final TextView subTitleView;
        private final TextView noView;

        private ChangDuanInfo changDuanInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.title);
            subTitleView = itemView.findViewById(R.id.subTitle);
            noView = itemView.findViewById(R.id.no);
            //当点击唱段时，通知观察者
            itemView.setOnClickListener(v -> changDuanObservable.setData(changDuanInfo));
        }

        public void setData(ChangDuanInfo changDuanInfo, int position) {
            noView.setText(String.valueOf(position + 1));
            titleView.setText(changDuanInfo.getName());
            //todo 设置剧种和剧目
            subTitleView.setText("剧种：剧目");
            this.changDuanInfo = changDuanInfo;
        }
    }
}
