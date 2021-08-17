package me.xlgp.douyinzimu;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.reactivex.rxjava3.functions.Consumer;
import me.xlgp.douyinzimu.adapter.ChangDuanListAdapter;
import me.xlgp.douyinzimu.db.AppDatabase;
import me.xlgp.douyinzimu.model.ChangDuan;
import me.xlgp.douyinzimu.service.ChangCiService;
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

    public void onFetch(View view) {

        try {
            new ChangDuanService().deleteAll();
            Toast.makeText(this, "唱段删除完毕", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "唱段删除完毕", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        try {
            new ChangCiService().deleteAll();
            Toast.makeText(this, "唱词删除完毕", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "删除唱词异常", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}