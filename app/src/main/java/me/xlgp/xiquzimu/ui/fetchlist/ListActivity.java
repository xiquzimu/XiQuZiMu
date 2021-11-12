package me.xlgp.xiquzimu.ui.fetchlist;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;
import java.util.function.Predicate;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import me.xlgp.xiquzimu.ZimuApplication;
import me.xlgp.xiquzimu.adapter.BaseAdapter.OnItemClickListener;
import me.xlgp.xiquzimu.config.FetchRepositoryConfig;
import me.xlgp.xiquzimu.data.ChangDuanRepository;
import me.xlgp.xiquzimu.databinding.ActivityListBinding;
import me.xlgp.xiquzimu.ui.base.BaseToolBarActivity;
import me.xlgp.xiquzimu.view.StringSearchRecyclerviewLayout;

public class ListActivity extends BaseToolBarActivity {

    ActivityListBinding binding = null;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private FetchViewModel fetchViewModel;
    StringSearchRecyclerviewLayout searchRecyclerviewLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("远程唱词列表");
        searchRecyclerviewLayout = binding.nameListSearchRecyclerviewLayout;
        searchRecyclerviewLayout.build(this);
        searchRecyclerviewLayout.setPredicate(new StringPredicate(searchRecyclerviewLayout.getFilterCharSequenceLiveData()));
        searchRecyclerviewLayout.setRefreshing(true);

        NameListAdapter nameListAdapter = new NameListAdapter();
        nameListAdapter.setOnItemClickListener(getOnItemClickListener());
        searchRecyclerviewLayout.setSearchListAdapter(nameListAdapter);

        fetchViewModel = new ViewModelProvider(this).get(FetchViewModel.class);

        searchRecyclerviewLayout.setOnRefreshListener(fetchViewModel::fetchNameList);

        fetchViewModel.getNameList().observe(this, list -> {
            searchRecyclerviewLayout.setRefreshing(false);
            if (list.size() == 0) {
                Snackbar.make(getWindow().getDecorView(), "无法获取远程唱段", Snackbar.LENGTH_SHORT).show();
            }
            setTotalCountTextView(list.size());
            nameListAdapter.updateData(list);
        });

        binding.fetchRadioGroup.setOnCheckedChangeListener(getOnCheckedChangeListener());
        initRadioButton();
    }

    private void initRadioButton() {
        if (FetchRepositoryConfig.getRepositoryType() == FetchRepositoryConfig.REPOSITORY_ENUM.GITEE) {
            binding.giteeRadioButton.setChecked(true);
        } else {
            binding.githubRadioButton.setChecked(true);
        }
    }

    private void fetchNameList() {
        searchRecyclerviewLayout.setRefreshing(true);
        fetchViewModel.fetchNameList();
    }

    private RadioGroup.OnCheckedChangeListener getOnCheckedChangeListener() {
        return (group, checkedId) -> {
            ZimuApplication zimuApplication = (ZimuApplication) getApplication();
            if (checkedId == binding.giteeRadioButton.getId()) {
                zimuApplication.setFetchRepositoryConfig(FetchRepositoryConfig.REPOSITORY_ENUM.GITEE);
            } else if (checkedId == binding.githubRadioButton.getId()) {
                zimuApplication.setFetchRepositoryConfig(FetchRepositoryConfig.REPOSITORY_ENUM.GITHUB);
            }
            fetchNameList();
        };
    }

    private void setTotalCountTextView(int count) {
        String total = "共有 " + count + " 条";
        binding.totalCountTextView.setText(total);
    }

    @Override
    protected void onPause() {
        super.onPause();
        searchRecyclerviewLayout.clearFocus();
    }

    private OnItemClickListener<String> getOnItemClickListener() {
        return (itemView, view, data, position) -> {
            Button button = (Button) view;
            CharSequence text = button.getText();
            button.setText("下载中...");
            button.setEnabled(false);
            new ChangDuanRepository().update(data).subscribe(new Observer<Long>() {

                Disposable disposable;

                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    disposable = d;
                }

                @Override
                public void onNext(@NonNull Long aLong) {
                    Snackbar.make(button, "更新成功", Snackbar.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    Snackbar.make(button, "更新失败" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onComplete() {
                    Snackbar.make(button, "获取唱段失败", Snackbar.LENGTH_SHORT).show();
                    finish();
                }

                private void finish() {
                    button.setText(text);
                    button.setEnabled(true);
                    if (disposable != null && !disposable.isDisposed()) {
                        disposable.dispose();
                    }
                }
            });
        };
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null) {
            compositeDisposable.clear();
            compositeDisposable = null;
        }
    }

    static class StringPredicate implements Predicate<String> {
        private final MutableLiveData<CharSequence> liveData;

        public StringPredicate(MutableLiveData<CharSequence> liveData) {
            this.liveData = liveData;
        }

        @Override
        public boolean test(String s) {
            try {
                return Objects.requireNonNull(s).contains(Objects.requireNonNull(liveData.getValue()));
            } catch (Exception e) {
                return false;
            }
        }
    }
}