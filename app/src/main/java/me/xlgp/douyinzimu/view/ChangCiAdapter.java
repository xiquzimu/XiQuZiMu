package me.xlgp.douyinzimu.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.obj.changduan.ChangCi;
import me.xlgp.douyinzimu.obj.changduan.ChangCiList;

public class ChangCiAdapter extends RecyclerView.Adapter<ChangCiAdapter.ViewHolder> {
    private ChangCiList changCiList;

    public ChangCiAdapter(ChangCiList changCiList) {
        this.changCiList = changCiList;
    }

    @NonNull
    @Override
    public ChangCiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zimu_item_layout, parent, false);
        return new ChangCiAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChangCiAdapter.ViewHolder holder, int position) {
        holder.setData(changCiList.get(position));
    }

    @Override
    public int getItemCount() {
        return changCiList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(ChangCi changCi) {
            Button button = itemView.findViewById(R.id.zimu_item_btn);
            button.setText(changCi.getContent());
            button.setOnClickListener(v -> Log.i("setData: ", changCi.getContent()));
        }

    }
}
