package me.xlgp.douyinzimu.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.designpatterns.ChangDuanObservable;
import me.xlgp.douyinzimu.obj.changduan.ChangDuan;

public class ChangDuanAdapter extends RecyclerView.Adapter<ChangDuanAdapter.ViewHolder> {
    private List<ChangDuan> changDuanList;
    private ChangDuanObservable<ChangDuan> changDuanObservable;

    public ChangDuanAdapter(List<ChangDuan> changDuanList, ChangDuanObservable<ChangDuan> observable) {
        this.changDuanList = changDuanList;
        this.changDuanObservable = observable;
    }

    public ChangDuanAdapter() {
        this(new ArrayList<>(), null);
    }

    public ChangDuanAdapter(ChangDuanObservable<ChangDuan> observable) {
        this(new ArrayList<>(), observable);
    }

    public void updateData(List<ChangDuan> list) {
        changDuanList = list;
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
        ChangDuan changDuan = changDuanList.get(position);
        holder.setData(changDuan);
    }

    @Override
    public int getItemCount() {
        return changDuanList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private Button button;
        private ChangDuan changDuan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.zimu_item_btn);
            button.setOnClickListener(new changDuanListener());
        }

        public void setData(ChangDuan changDuan) {
            button.setText(changDuan.getChangeDuanQiTa().getTitle());
            this.changDuan = changDuan;
        }

        private class changDuanListener implements View.OnClickListener {


            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), changDuan.getChangeDuanQiTa().getTitle() + " - " + changDuan.getChangeDuanQiTa().getJuMu(), Toast.LENGTH_SHORT).show();
                changDuanObservable.setData(changDuan);
            }
        }
    }
}
