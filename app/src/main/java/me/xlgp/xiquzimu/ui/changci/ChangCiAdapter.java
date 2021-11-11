package me.xlgp.xiquzimu.ui.changci;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import org.jetbrains.annotations.NotNull;

import me.xlgp.xiquzimu.R;
import me.xlgp.xiquzimu.adapter.BaseAdapter;
import me.xlgp.xiquzimu.databinding.ChangCiItemBinding;
import me.xlgp.xiquzimu.model.ChangCi;

public class ChangCiAdapter extends BaseAdapter<ChangCi> {

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = getInflatedView(R.layout.chang_ci_item, parent, false);
        return new ViewHolder(view);
    }

    public int addItem() {
        ChangCi changCi = new ChangCi();
        list.add(changCi);
        notifyItemInserted(getItemCount());
        notifyItemChanged(getItemCount());
        return getItemCount() - 1;
    }

    public void removeItem(int position) {
        notifyItemRemoved(position);
        list.remove(position);
        //受影响的item都刷新position
        notifyItemRangeChanged(position, list.size() - 1);
    }

    static class ViewHolder extends BaseAdapter.ViewHolder<ChangCi> {

        private final ChangCiItemBinding binding;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = ChangCiItemBinding.bind(itemView);
            binding.remove.setOnClickListener(this::createAlertDialog);
        }

        private void createAlertDialog(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle("删除")
                    .setMessage("请选择操作")
                    .setPositiveButton("删除该项", (dialog, which) -> onItemClickListener.onItemClick(itemView, v, data, getAdapterPosition()))
                    .setNegativeButton("取消", (dialog, which) -> {
                    }).show();
        }

        @Override
        public void setData(ChangCi data) {
            super.setData(data);
            binding.editTextContent.setText(data.getContent());
            binding.editTextShowTime.setText(data.getShowTime());
        }
    }
}
