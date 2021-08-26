package me.xlgp.douyinzimu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import me.xlgp.douyinzimu.ui.main.SearchRecyclerviewLayout;

public class SearchRecycleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recycle);
        SearchRecyclerviewLayout searchRecyclerviewLayout = findViewById(R.id.searchRecyclerviewLayout);
        searchRecyclerviewLayout.build(this);
    }
}