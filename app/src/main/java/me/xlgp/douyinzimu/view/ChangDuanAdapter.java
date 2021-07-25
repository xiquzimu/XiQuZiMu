package me.xlgp.douyinzimu.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.obj.changduan.ChangDuan;
import me.xlgp.douyinzimu.service.PingLunService;

public class ChangDuanAdapter extends RecyclerView.Adapter<ChangDuanAdapter.ViewHolder> {
    private List<ChangDuan> changDuanList;
    private TextView textView;
    public ChangDuanAdapter(List<ChangDuan> changDuanList, TextView textView) {
        this.changDuanList = changDuanList;
        this.textView = textView;
    }

    @NonNull
    @Override
    public ChangDuanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zimu_item_layout, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        return holder;
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

        private class changDuanListener implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), changDuan.getChangeDuanQiTa().getTitle() + " - " + changDuan.getChangeDuanQiTa().getJuMu(), Toast.LENGTH_SHORT).show();
                PingLunService.getInstance().setChangeCiList(changDuan.getChangeCiList(0));
                textView.setText(changDuan.getChangeDuanQiTa().getTitle() + " - " + changDuan.getChangeDuanQiTa().getJuMu());
            }
        }

        public void setData(ChangDuan changDuan) {
            button.setText(changDuan.getChangeDuanQiTa().getTitle());
            this.changDuan = changDuan;
        }
    }
}
