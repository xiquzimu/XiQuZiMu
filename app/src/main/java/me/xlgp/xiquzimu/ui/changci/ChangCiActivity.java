package me.xlgp.xiquzimu.ui.changci;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import me.xlgp.xiquzimu.R;
import me.xlgp.xiquzimu.constant.JuZhongConstant;
import me.xlgp.xiquzimu.databinding.ActivityChangCiBinding;
import me.xlgp.xiquzimu.model.ChangDuan;
import me.xlgp.xiquzimu.model.ChangDuanInfo;
import me.xlgp.xiquzimu.ui.base.BaseToolBarActivity;
import me.xlgp.xiquzimu.util.ChangDuanHelper;
import me.xlgp.xiquzimu.util.CopyHelper;

public class ChangCiActivity extends BaseToolBarActivity implements PopupMenu.OnMenuItemClickListener {

    private ActivityChangCiBinding bing;
    private ChangCiAdapter changCiAdapter;
    private ChangCiViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bing = ActivityChangCiBinding.inflate(getLayoutInflater());
        setContentView(bing.getRoot());
        setTitle("唱词列表");

        int changduanID = getIntent().getIntExtra("changduanID", -1);

        viewModel = new ViewModelProvider(this).get(ChangCiViewModel.class);

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
        bing.copy.setOnClickListener(this::copy);
    }

    private void copy(View view) {
        ChangDuanInfo changDuanInfo = viewModel.getChangDuanInfo().getValue();
        if (changDuanInfo == null) {
            Toast.makeText(this, "无数据可复制", Toast.LENGTH_SHORT).show();
            return;
        }
        initPopupMenu(view);
    }

    private void initPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.copy_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
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
        int position = changCiAdapter.addItem();
        bing.recyclerView.smoothScrollToPosition(position);
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

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        ChangDuanInfo changDuanInfo = viewModel.getChangDuanInfo().getValue();
        if (changDuanInfo == null) {
            Toast.makeText(this, "无数据可复制", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (item.getItemId() == R.id.item_chunjing) {
            CopyHelper.copy(ChangDuanHelper.copyChunJingFromChangDuanInfo(changDuanInfo), getApplication());
        } else if (item.getItemId() == R.id.item_zimu) {
            CopyHelper.copy(ChangDuanHelper.copyFromChangDuanInfo(changDuanInfo), getApplication());
        }
        Toast.makeText(this, "已复制：" + changDuanInfo.getChangDuan().getName(), Toast.LENGTH_SHORT).show();
        return false;
    }
}