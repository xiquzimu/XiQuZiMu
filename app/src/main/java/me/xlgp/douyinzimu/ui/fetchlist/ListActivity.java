package me.xlgp.douyinzimu.ui.fetchlist;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;
import java.util.function.Predicate;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import me.xlgp.douyinzimu.adapter.BaseAdapter.OnItemClickListener;
import me.xlgp.douyinzimu.databinding.ActivityListBinding;
import me.xlgp.douyinzimu.data.ChangDuanRepository;
import me.xlgp.douyinzimu.view.SearchRecyclerviewLayout;

public class ListActivity extends AppCompatActivity {

    ActivityListBinding binding = null;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SearchRecyclerviewLayout<String> searchRecyclerviewLayout = binding.nameListSearchRecyclerviewLayout;
        searchRecyclerviewLayout.build(this);
        searchRecyclerviewLayout.setPredicate(new StringPredicate(searchRecyclerviewLayout.getFilterCharSequenceLiveData()));
        searchRecyclerviewLayout.setRefreshing(true);

        NameListAdapter nameListAdapter = new NameListAdapter();
        nameListAdapter.setOnItemClickListener(getOnItemClickListener());
        searchRecyclerviewLayout.setSearchListAdapter(nameListAdapter);

        FetchViewModel fetchViewModel = new ViewModelProvider(this).get(FetchViewModel.class);

        searchRecyclerviewLayout.setOnRefreshListener(fetchViewModel::fetchNameList);

        fetchViewModel.getNameList().observe(this, list -> {
            searchRecyclerviewLayout.setRefreshing(false);
            if (list.size() == 0) {
                Toast.makeText(this, "无法获取远程唱词", Toast.LENGTH_SHORT).show();
            }
            setTotalCountTextView(list.size());
            nameListAdapter.updateData(list);
        });
    }

    @SuppressLint("SetTextI18n")
    private void setTotalCountTextView(int count) {
        binding.totalCountTextView.setText("共有 " + count + " 条");
    }

    private OnItemClickListener<String> getOnItemClickListener() {
        return (itemView, view, data, position) -> {
            Button button = (Button) view;
            CharSequence text = button.getText();
            button.setText("更新...");
            button.setEnabled(false);
            compositeDisposable.add(new ChangDuanRepository().update(data).subscribe(id -> {
                Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
                button.setText(text);
                button.setEnabled(true);
            }, throwable -> {
                Toast.makeText(this, "更新失败", Toast.LENGTH_SHORT).show();
                button.setText(text);
                button.setEnabled(true);
            }));
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