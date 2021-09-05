package me.xlgp.douyinzimu;

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
import me.xlgp.douyinzimu.adapter.NameListAdapter;
import me.xlgp.douyinzimu.service.ChangDuanService;
import me.xlgp.douyinzimu.ui.main.SearchRecyclerviewLayout;
import me.xlgp.douyinzimu.viewmodel.FetchViewModel;

public class ListActivity extends AppCompatActivity {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        SearchRecyclerviewLayout<String> searchRecyclerviewLayout = findViewById(R.id.nameListSearchRecyclerviewLayout);
        searchRecyclerviewLayout.build(this);
        searchRecyclerviewLayout.setPredicate(new StringPredicate(searchRecyclerviewLayout.getFilterCharSequenceLiveData()));
        searchRecyclerviewLayout.setRefreshing(true);

        NameListAdapter nameListAdapter = new NameListAdapter();
        nameListAdapter.setOnItemClickListener(getOnItemClickListener());
        searchRecyclerviewLayout.setSearchListAdapter(nameListAdapter);

        FetchViewModel fetchViewModel = new ViewModelProvider(this).get(FetchViewModel.class);

        fetchViewModel.getNameList().observe(this, list -> {
            searchRecyclerviewLayout.setRefreshing(false);
            if (list.size() == 0) {
                Toast.makeText(this, "无法获取远程唱词", Toast.LENGTH_SHORT).show();
                return;
            }
            nameListAdapter.updateData(list);
        });
    }

    private OnItemClickListener<String> getOnItemClickListener() {
        return (itemView, view, data, position) -> {
            Button button = (Button) view;
            CharSequence text = button.getText();
            button.setText("更新...");
            compositeDisposable.add(new ChangDuanService().update(data).subscribe(id -> {
                Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
                button.setText(text);
            }, throwable -> {
                Toast.makeText(this, "更新失败", Toast.LENGTH_SHORT).show();
                button.setText(text);
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