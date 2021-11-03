package me.xlgp.xiquzimu.ui.changci;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import me.xlgp.xiquzimu.R;
import me.xlgp.xiquzimu.constant.JuZhongConstant;
import me.xlgp.xiquzimu.databinding.ActivityChangCiBinding;
import me.xlgp.xiquzimu.model.ChangDuan;
import me.xlgp.xiquzimu.ui.base.BaseToolBarActivity;

public class ChangCiActivity extends BaseToolBarActivity {

    private ActivityChangCiBinding bing;
    private ChangCiAdapter changCiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bing = ActivityChangCiBinding.inflate(getLayoutInflater());
        setContentView(bing.getRoot());
        setTitle("唱词列表");

        int changduanID = getIntent().getIntExtra("changduanID", -1);

        ChangCiViewModel viewModel = new ViewModelProvider(this).get(ChangCiViewModel.class);

        changCiAdapter = new ChangCiAdapter();

        initRecyclerView();

        viewModel.getChangDuanInfo().observe(this, changDuanInfo -> {
            changCiAdapter.updateData(changDuanInfo.getChangeCiList());
            initView(changDuanInfo.getChangDuan());
            setSpinner(changDuanInfo.getChangDuan());
        });

        viewModel.state.observe(this, s -> Snackbar.make(bing.getRoot(), s, Snackbar.LENGTH_SHORT).show());

        viewModel.loadData(changduanID);

        initSpinner();

        bing.save.setOnClickListener(this::save);
        bing.add.setOnClickListener(this::add);
    }

    private void initSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.changci_spinner_item, JuZhongConstant.list());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bing.juMuSpinner.setAdapter(adapter);
    }

    private void initRecyclerView() {
        bing.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bing.recyclerView.setAdapter(changCiAdapter);
        changCiAdapter.setOnItemClickListener((itemView, view, data, position) -> changCiAdapter.removeItem(position));
    }

    private void save(View view) {
    }

    public void add(View view) {
        changCiAdapter.addItem();
    }

    private void initView(ChangDuan changDuan) {
        bing.editTextJuMu.setText(changDuan.getJuMu());

        bing.editTextOffset.setText(String.valueOf(changDuan.getOffset()));
        bing.editTextName.setText(changDuan.getName());
    }

    private void setSpinner(ChangDuan changDuan) {
        if (changDuan == null) return;
        String juZhong = changDuan.getJuZhong();
        List<String> list = JuZhongConstant.list();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(juZhong)) {
                bing.juMuSpinner.setSelection(i);
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bing = null;
    }
}