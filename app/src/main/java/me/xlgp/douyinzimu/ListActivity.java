package me.xlgp.douyinzimu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import me.xlgp.douyinzimu.adapter.NameListAdapter;
import me.xlgp.douyinzimu.service.ChangDuanService;
import me.xlgp.douyinzimu.service.FetchGiteeService;
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
        searchRecyclerviewLayout.setPredicate(s -> {
            try {
                return Objects.requireNonNull(s).contains(searchRecyclerviewLayout.getFilterCharSequence());
            } catch (Exception e) {
                return false;
            }
        });

        NameListAdapter nameListAdapter = new NameListAdapter();
        nameListAdapter.setOnItemClickListener((itemView, data, position) -> new ChangDuanService(compositeDisposable).update(data, Throwable::printStackTrace));
        searchRecyclerviewLayout.setSearchListAdapter(nameListAdapter);

        FetchViewModel fetchViewModel = new ViewModelProvider(this).get(FetchViewModel.class);

        new FetchGiteeService().getNameList(fetchViewModel.getNameList());
        fetchViewModel.getNameList().observe(this, nameListAdapter::updateData);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null) {
            compositeDisposable.clear();
            compositeDisposable = null;
        }
    }
}