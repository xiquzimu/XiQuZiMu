package me.xlgp.douyinzimu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.xlgp.douyinzimu.adapter.NameListAdapter;
import me.xlgp.douyinzimu.db.AppDatabase;
import me.xlgp.douyinzimu.service.ChangDuanService;
import me.xlgp.douyinzimu.service.FetchGiteeService;
import me.xlgp.douyinzimu.viewmodel.FetchViewModel;

public class FetchActivity extends AppCompatActivity {

    private FetchViewModel fetchViewModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch);

        AppDatabase.build(this);

        RecyclerView recyclerView = findViewById(R.id.fetchNameList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        NameListAdapter nameListAdapter = new NameListAdapter();
        recyclerView.setAdapter(nameListAdapter);
        nameListAdapter.setOnItemClickListener((itemView, data, position) -> {
            new ChangDuanService().update(data.substring(1), throwable -> throwable.printStackTrace());
        });

        fetchViewModel = new ViewModelProvider(this).get(FetchViewModel.class);

        new FetchGiteeService().getNameList(fetchViewModel.getNameList());
        fetchViewModel.getNameList().observe(this, strings -> nameListAdapter.updateData(strings));
    }
}