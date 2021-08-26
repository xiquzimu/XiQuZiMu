package me.xlgp.douyinzimu.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BaseAdapter<E> extends RecyclerView.Adapter<BaseAdapter.ViewHolder<E>> {
    protected OnItemClickListener<E> onItemClickListener;
    protected List<E> list;

    public BaseAdapter() {
        this.list = new ArrayList<E>();
    }

    public BaseAdapter(List<E> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public BaseAdapter.ViewHolder<E> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseAdapter.ViewHolder<E> holder, int position) {
        holder.setData(list.get(position));
        holder.setOnItemClickListener(onItemClickListener);
    }

    public void updateData(List<E> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnItemClickListener(OnItemClickListener<E> listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener<E> {
        void onItemClick(View itemView, E data, int position);
    }

    protected static class ViewHolder<E> extends RecyclerView.ViewHolder {

        protected E data;
        protected OnItemClickListener<E> onItemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setOnItemClickListener(OnItemClickListener<E> onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public void setData(E data) {
            this.data = data;
        }
    }
}
