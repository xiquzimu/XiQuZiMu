package me.xlgp.douyinzimu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.reactivex.rxjava3.functions.Consumer;
import me.xlgp.douyinzimu.adapter.ChangDuanListAdapter;
import me.xlgp.douyinzimu.db.AppDatabase;
import me.xlgp.douyinzimu.model.ChangDuan;
import me.xlgp.douyinzimu.service.ChangDuanService;
import me.xlgp.douyinzimu.viewmodel.ChangDuanViewModel;

public class ZimuListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zimu_list);

        AppDatabase.build(this);

        RecyclerView recyclerView = findViewById(R.id.changduanListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ChangDuanListAdapter changDuanListAdapter = new ChangDuanListAdapter();
        recyclerView.setAdapter(changDuanListAdapter);

        ChangDuanViewModel viewModel = new ViewModelProvider(this).get(ChangDuanViewModel.class);

        new ChangDuanService().list(new Consumer<List<ChangDuan>>() {
            @Override
            public void accept(List<ChangDuan> changDuanList) throws Throwable {
                viewModel.getChangduanList().setValue(changDuanList);
            }
        });

        viewModel.getChangduanList().observe(this, changDuanListAdapter::updateData);
    }
}