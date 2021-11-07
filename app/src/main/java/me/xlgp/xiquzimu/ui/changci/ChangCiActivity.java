package me.xlgp.xiquzimu.ui.changci;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

public class ChangCiActivity extends BaseToolBarActivity {

    private ActivityChangCiBinding bing;
    private ChangCiAdapter changCiAdapter;
    private ChangCiViewModel viewModel;

    private enum CHANGCI_TYPE_ENMU {
        ZIMU,//字幕版
        CHUNJING//纯净版

    }

    private enum SELECT_ITEM_ENMU {
        SHARE,
        COPY
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bing = ActivityChangCiBinding.inflate(getLayoutInflater());
        setContentView(bing.getRoot());
        setTitle("唱词");
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

        bing.add.setOnClickListener(this::add);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.changci_toolbar_menu, menu);
        return true;
    }

    public Dialog createDialog(SELECT_ITEM_ENMU enmu) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择复制类型")
                .setItems(R.array.copy_item_array, (dialog, which) -> {
                    switch (enmu) {
                        case SHARE:
                            share(CHANGCI_TYPE_ENMU.values()[which]);
                            break;
                        case COPY:
                            copy(CHANGCI_TYPE_ENMU.values()[which]);
                    }
                    dialog.dismiss();
                });
        return builder.create();
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

    public void share(CHANGCI_TYPE_ENMU enmu) {
        String text = getCopyText(enmu);
        ChangDuanInfo changDuanInfo = viewModel.getChangDuanInfo().getValue();
        if (changDuanInfo == null) {
            Toast.makeText(this, "数据为空", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
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

    private String getCopyText(CHANGCI_TYPE_ENMU enmu) {
        ChangDuanInfo changDuanInfo = viewModel.getChangDuanInfo().getValue();
        if (changDuanInfo == null) {
            return "";
        }
        switch (enmu) {
            case CHUNJING:
                return ChangDuanHelper.copyChunJingFromChangDuanInfo(changDuanInfo).toString();
            case ZIMU:
                return ChangDuanHelper.copyFromChangDuanInfo(changDuanInfo).toString();
            default:
                return "";
        }
    }

    /**
     * 复制唱词
     *
     * @param enmu 表示类型，纯净版，字幕版
     */
    public void copy(CHANGCI_TYPE_ENMU enmu) {
        String text = getCopyText(enmu);
        if ("".equals(text)) {
            Toast.makeText(this, "无数据可复制", Toast.LENGTH_SHORT).show();
            return;
        }
        CopyHelper.copy(ClipData.newPlainText("唱词", text), getApplication());
        Toast.makeText(this, "已复制", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_copy) {
            createDialog(SELECT_ITEM_ENMU.COPY).show();
        } else if (item.getItemId() == R.id.item_share) {
            createDialog(SELECT_ITEM_ENMU.SHARE).show();
        }
        return super.onOptionsItemSelected(item);
    }
}