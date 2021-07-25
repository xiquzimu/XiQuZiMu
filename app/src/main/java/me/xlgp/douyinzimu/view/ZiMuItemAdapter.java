package me.xlgp.douyinzimu.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.obj.changduan.ChangeDuan;
import me.xlgp.douyinzimu.service.PingLunService;

public class ZiMuItemAdapter extends RecyclerView.Adapter<ZiMuItemAdapter.ViewHolder> {
    private List<ChangeDuan> changeDuanList;

    public ZiMuItemAdapter(List<ChangeDuan> changeDuanList) {
        this.changeDuanList = changeDuanList;
    }

    @NonNull
    @Override
    public ZiMuItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zimu_item_layout, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ZiMuItemAdapter.ViewHolder holder, int position) {
        ChangeDuan changeDuan = changeDuanList.get(position);
        holder.setData(changeDuan);
    }

    @Override
    public int getItemCount() {
        return changeDuanList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private Button button;
        private ChangeDuan changeDuan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.zimu_item_btn);
            button.setOnClickListener((v) -> {
                Toast.makeText(v.getContext(), button.getText(), Toast.LENGTH_SHORT).show();
                PingLunService.getInstance().setChangeCiList(changeDuan.getChangeCiList());
            });
        }

        public void setData(ChangeDuan changeDuan) {
            button.setText(changeDuan.getChangeDuanQiTa().getTitle());
            this.changeDuan = changeDuan;
        }
    }
}
