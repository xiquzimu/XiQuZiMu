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
import me.xlgp.douyinzimu.obj.ZiMuItem;
import me.xlgp.douyinzimu.service.PingLunService;
import me.xlgp.douyinzimu.zimu.NvfumaZimu;

public class ZiMuItemAdapter extends RecyclerView.Adapter<ZiMuItemAdapter.ViewHolder> {
    private List<ZiMuItem> ziMuItemList;

    public ZiMuItemAdapter(List<ZiMuItem> list) {
        this.ziMuItemList = list;
    }

    public ZiMuItemAdapter(ZiMuItem item) {
        if (ziMuItemList == null) {
            ziMuItemList = new ArrayList<>();
        } else if (!ziMuItemList.isEmpty()) {
            ziMuItemList.clear();
        }
        ziMuItemList.add(item);
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
        ZiMuItem ziMuItem = ziMuItemList.get(position);
        holder.setData(ziMuItem);
    }

    @Override
    public int getItemCount() {
        return ziMuItemList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.zimu_item_btn);
            button.setOnClickListener((v) -> {
                Toast.makeText(v.getContext(), button.getText(), Toast.LENGTH_SHORT).show();
                PingLunService.getInstance().setZiMuList(NvfumaZimu.getZhongZhuangYuan());
            });
        }

        public void setData(ZiMuItem ziMuItem) {
            button.setText(ziMuItem.getTitle());
        }
    }
}
