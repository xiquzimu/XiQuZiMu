package me.xlgp.xiquzimu.ui.changci;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

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

    static class ViewHolder extends BaseAdapter.ViewHolder<ChangCi> {

        private final ChangCiItemBinding binding;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = ChangCiItemBinding.bind(itemView);
        }

        @Override
        public void setData(ChangCi data) {
            super.setData(data);
            binding.editTextContent.setText(data.getContent());
            binding.editTextShowTime.setText(data.getShowTime());
        }
    }
}
