package me.xlgp.douyinzimu.view;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.promeg.pinyinhelper.Pinyin;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import me.xlgp.douyinzimu.ZimuApplication;
import me.xlgp.douyinzimu.databinding.ZimuFloatingLayoutBinding;
import me.xlgp.douyinzimu.designpatterns.ChangDuanData;
import me.xlgp.douyinzimu.model.ChangDuan;
import me.xlgp.douyinzimu.obj.Callback;
import me.xlgp.douyinzimu.service.ChangDuanService;

public class ZimuListFloatinglayout {
    private final View rootLayout;
    private final Context context;
    private final ZimuMainFloatingLayout.ChangDuanObservable changDuanObservable;
    private final CompositeDisposable compositeDisposable;

    ChangDuanAdapter changDuanAdapter;
    private final ZimuFloatingLayoutBinding binding;

    public ZimuListFloatinglayout(View view, ZimuMainFloatingLayout.ChangDuanObservable changDuanObservable) {
        rootLayout = view;
        binding = ZimuFloatingLayoutBinding.bind(view);
        this.context = view.getContext();
        this.changDuanObservable = changDuanObservable;
        this.compositeDisposable = ZimuApplication.getCompositeDisposable();

        ChangDuanData changDuanData = ChangDuanData.getInstance();
        changDuanAdapter = new ChangDuanAdapter();

        initRecyclerView();

        changDuanData.observe((o, arg) ->
                binding.currentZimuTitleTextView.setText(((ChangDuanData) o)
                .getData().getChangDuan().getName()));

        initSwipeRefreshLayout();
    }

    private void initSwipeRefreshLayout() {
        binding.zimuListSwipeRefreshLayout.setOnRefreshListener(() ->
                loadData(aBoolean -> binding.zimuListSwipeRefreshLayout.setRefreshing(false)));
        binding.zimuListSwipeRefreshLayout.setRefreshing(true);
        loadData(aBoolean -> binding.zimuListSwipeRefreshLayout.setRefreshing(false));
    }

    private void sortByPinYin(List<ChangDuan> list) {
        list.sort((o1, o2) -> {
            try {
                return Pinyin.toPinyin(o1.getJuMu(), "").compareTo(Pinyin.toPinyin(o2.getJuMu(), ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        });
    }

    private void loadData(Callback<Boolean> callback) {
        ChangDuanService changDuanService = new ChangDuanService(compositeDisposable);
        changDuanService.list().subscribe(list -> {
            if (list == null || list.size() == 0) {
                Toast.makeText(context, "无数据可更新", Toast.LENGTH_SHORT).show();
                return;
            }
            sortByPinYin(list);
            changDuanAdapter.updateData(list);
            if (callback != null) callback.call(true);
        }, throwable -> Toast.makeText(context, "获取唱段异常", Toast.LENGTH_SHORT).show());
    }

    private void initRecyclerView() {

        binding.zimuListRecyclerview.setLayoutManager(new LinearLayoutManager(context));
        changDuanAdapter.setOnItemClickListener((itemView, view, data, position) -> changDuanObservable.setData(data));
        binding.zimuListRecyclerview.setAdapter(changDuanAdapter);
    }
}
