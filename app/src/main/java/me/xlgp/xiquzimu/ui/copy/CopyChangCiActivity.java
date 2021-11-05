package me.xlgp.xiquzimu.ui.copy;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import me.xlgp.xiquzimu.databinding.ActivityCopyChangCiBinding;
import me.xlgp.xiquzimu.model.ChangDuanInfo;
import me.xlgp.xiquzimu.ui.base.BaseToolBarActivity;
import me.xlgp.xiquzimu.util.ChangDuanHelper;

public class CopyChangCiActivity extends BaseToolBarActivity {

    private ActivityCopyChangCiBinding binding;
    private CopyChangCiViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCopyChangCiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("复制唱词");
        int changduanID = getIntent().getIntExtra("changduanID", -1);

        viewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(CopyChangCiViewModel.class);
        viewModel.getChangDuanInfo().observe(this, new Observer<ChangDuanInfo>() {
            @Override
            public void onChanged(ChangDuanInfo changDuanInfo) {
            }
        });

        viewModel.state.observe(this, s -> Snackbar.make(binding.getRoot(), s, Snackbar.LENGTH_SHORT).show());

        viewModel.loadData(changduanID);

        binding.copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copy(viewModel.getChangDuanInfo().getValue());
            }
        });
    }

    private void copy(ChangDuanInfo changDuanInfo) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ChangDuanHelper.copyFromChangDuanInfo(changDuanInfo);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(this, "已复制", Toast.LENGTH_SHORT).show();
    }
}