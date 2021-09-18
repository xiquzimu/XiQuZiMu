package me.xlgp.douyinzimu.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<E> extends RecyclerView.Adapter<BaseAdapter.ViewHolder<E>> {
    protected OnItemClickListener<E> onItemClickListener;
    protected List<E> list;

    public BaseAdapter() {
        this.list = new ArrayList<>();
    }

    public BaseAdapter(List<E> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public BaseAdapter.ViewHolder<E> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        throw new NullPointerException("BaseAdapter.ViewHolder is null");
    }

    protected View getInflatedView(int resource, @NonNull ViewGroup parent, boolean attachToRoot) {
        return LayoutInflater.from(parent.getContext()).inflate(resource, parent, attachToRoot);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseAdapter.ViewHolder<E> holder, int position) {
        holder.setData(list.get(position));
        holder.setOnItemClickListener(onItemClickListener);
    }

    @SuppressLint("NotifyDataSetChanged")
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
        /**
         * @param itemView 当前项 view
         * @param view     发出事件view
         * @param data     当前项的数据
         * @param position 当前项的position
         */
        void onItemClick(View itemView, View view, E data, int position);
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
