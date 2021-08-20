package me.xlgp.douyinzimu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import me.xlgp.douyinzimu.adapter.NameListAdapter;
import me.xlgp.douyinzimu.service.ChangDuanService;
import me.xlgp.douyinzimu.service.FetchGiteeService;
import me.xlgp.douyinzimu.viewmodel.FetchViewModel;

public class ListActivity extends AppCompatActivity {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        RecyclerView recyclerView = findViewById(R.id.fetchNameList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        NameListAdapter nameListAdapter = new NameListAdapter();
        nameListAdapter.setOnItemClickListener((itemView, data, position) -> new ChangDuanService(compositeDisposable).update(data, Throwable::printStackTrace));
        recyclerView.setAdapter(nameListAdapter);

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