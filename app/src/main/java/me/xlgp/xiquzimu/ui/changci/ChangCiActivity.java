package me.xlgp.xiquzimu.ui.changci;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;

import me.xlgp.xiquzimu.databinding.ActivityChangCiBinding;
import me.xlgp.xiquzimu.model.ChangDuan;

public class ChangCiActivity extends AppCompatActivity {

    private ActivityChangCiBinding bing;
    private ChangCiAdapter changCiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bing = ActivityChangCiBinding.inflate(getLayoutInflater());
        setContentView(bing.getRoot());

        int changduanID = getIntent().getIntExtra("changduanID", -1);

        ChangCiViewModel viewModel = new ViewModelProvider(this).get(ChangCiViewModel.class);

        changCiAdapter = new ChangCiAdapter();

        initRecyclerView();

        viewModel.getChangDuanInfo().observe(this, changDuanInfo -> {
            changCiAdapter.updateData(changDuanInfo.getChangeCiList());
            initView(changDuanInfo.getChangDuan());
        });

        viewModel.state.observe(this, s -> Snackbar.make(bing.getRoot(), s, Snackbar.LENGTH_SHORT).show());

        viewModel.loadData(changduanID);

        bing.save.setOnClickListener(this::save);
    }

    private void initRecyclerView() {
        bing.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bing.recyclerView.setAdapter(changCiAdapter);
    }

    private void save(View view) {
    }

    private void initView(ChangDuan changDuan) {
        bing.editTextJuMu.setText(changDuan.getJuMu());
        bing.editTextJuZhong.setText(changDuan.getJuZhong());
        bing.editTextOffset.setText(String.valueOf(changDuan.getOffset()));
        bing.editTextName.setText(changDuan.getName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bing = null;
    }
}