package me.xlgp.douyinzimu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import me.xlgp.douyinzimu.adapter.NameListAdapter;
import me.xlgp.douyinzimu.service.FetchGiteeService;
import me.xlgp.douyinzimu.viewmodel.FetchViewModel;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        RecyclerView recyclerView = findViewById(R.id.fetchNameList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        NameListAdapter nameListAdapter = new NameListAdapter();
        recyclerView.setAdapter(nameListAdapter);

        FetchViewModel fetchViewModel = new ViewModelProvider(this).get(FetchViewModel.class);

        new FetchGiteeService().getNameList(fetchViewModel.getNameList());
        fetchViewModel.getNameList().observe(this, nameListAdapter::updateData);
    }
}