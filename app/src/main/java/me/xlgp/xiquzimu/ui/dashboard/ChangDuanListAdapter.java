package me.xlgp.xiquzimu.ui.dashboard;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import me.xlgp.xiquzimu.R;
import me.xlgp.xiquzimu.adapter.BaseAdapter;
import me.xlgp.xiquzimu.adapter.SearchListAdapter;
import me.xlgp.xiquzimu.data.ChangDuanRepository;
import me.xlgp.xiquzimu.databinding.ChangduanItemLayoutBinding;
import me.xlgp.xiquzimu.params.TagChangDuan;

public class ChangDuanListAdapter extends SearchListAdapter<TagChangDuan> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(getInflatedView(R.layout.changduan_item_layout, parent, false));
    }

    public static class GroupHeaderItemDecoration extends RecyclerView.ItemDecoration {
        private final ChangDuanListAdapter changDuanListAdapter;
        private final int groupHeaderHeight = 40;

        public GroupHeaderItemDecoration(ChangDuanListAdapter changDuanListAdapter) {
            this.changDuanListAdapter = changDuanListAdapter;
        }

        private String getTagLine(int position) {
            List<TagChangDuan> list = changDuanListAdapter.filterList;
            try {
                if (list != null && !list.isEmpty() &&
                        (position == 0 || !list.get(position).getTag().equals(list.get(position - 1).getTag()))) {
                    return list.get(position).getTag();
                }
            } catch (Exception e) {
                return null;
            }
            return null;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int position = parent.getChildAdapterPosition(view);

            //ItemView的position==0 或者 当前ItemView的data的tag和上一个ItemView的不相等，则为当前ItemView设置top 偏移量
            if (getTagLine(position) != null) {
                outRect.set(0, groupHeaderHeight, 0, 0);
            }
        }

        @Override
        public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.onDraw(c, parent, state);
            for (int i = 0; i < parent.getChildCount(); i++) {
                View view = parent.getChildAt(i);
                int position = parent.getChildAdapterPosition(view);
                String tag = getTagLine(position);
                if (tag != null) {
                    drawGroupHeader(c, parent, view, tag);
                }
            }
        }

        @Override
        public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.onDrawOver(c, parent, state);
        }

        private void drawGroupHeader(Canvas c, RecyclerView parent, View view, String tag) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
            int left = parent.getPaddingLeft();
            int bottom = view.getTop() - params.topMargin;
            int right = parent.getWidth() - parent.getPaddingRight();
            int top = bottom - groupHeaderHeight;

            Paint mPaint = new Paint();
            mPaint.setARGB(225, 205, 205, 205);
            c.drawRect(left, top, right, bottom, mPaint);

            Paint mTextPaint = new TextPaint();
            mTextPaint.setTextSize(28);
            mTextPaint.setARGB(225, 100, 100, 100);
            int x = left + 30;
            int y = top + 30;
            c.drawText(tag, x, y, mTextPaint);
        }
    }

    protected static class ViewHolder extends BaseAdapter.ViewHolder<TagChangDuan> {
        private final ChangduanItemLayoutBinding binding;

        public ViewHolder(View view) {
            super(view);
            binding = ChangduanItemLayoutBinding.bind(view);
            view.setOnClickListener(v -> onItemClickListener.onItemClick(view,view, data, getAdapterPosition()));
            view.setOnLongClickListener(v -> {
                createAlertDialog();
                return false;
            });
        }

        private void deleteItem() {

            try {
                new ChangDuanRepository(new CompositeDisposable())
                        .delete(data, s -> Snackbar.make(itemView, "删除成功", Snackbar.LENGTH_SHORT).show());
            } catch (Exception e) {
                Snackbar.make(itemView, "删除数据失败", Snackbar.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        private void createAlertDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle("删除")
                    .setMessage(data.getJuMu() + " " + data.getName())
                    .setPositiveButton("确定", (dialog, which) -> deleteItem())
                    .setNegativeButton("取消", (dialog, which) -> {
                    }).show();
        }

        @Override
        public void setData(TagChangDuan changDuan) {
            super.setData(changDuan);
            binding.no.setText(String.valueOf(this.getAdapterPosition()));
            String title = changDuan.getJuZhong() + " " + changDuan.getJuMu();
            binding.title.setText(title);
            binding.content.setText(changDuan.getName());
        }
    }
}
