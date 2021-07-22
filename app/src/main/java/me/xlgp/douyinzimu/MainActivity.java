package me.xlgp.douyinzimu;

import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import me.xlgp.douyinzimu.constant.SysSettingsContant;
import me.xlgp.douyinzimu.service.FloatingService;
import me.xlgp.douyinzimu.util.AccessibilitySettingsHelper;
import me.xlgp.douyinzimu.util.CopyUtil;
import me.xlgp.douyinzimu.util.FloatingHelper;

public class MainActivity extends AppCompatActivity {

    private ClipboardManager clipboardManager = null;
    private static final int FLOATING_CODE = 1;
    private static final int ACCESSIBILITY_CODE = 2;
    private Intent floatingIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
    }

    public void onCopyAndPaste(View view) {
        CopyUtil.copy(clipboardManager, new Random().nextFloat());
        String pasteText = CopyUtil.paste(clipboardManager);
        TextView pasteTextView = findViewById(R.id.pasteTextView);
        pasteTextView.setText(pasteText);
    }

    public void onStartFloatingService(View view) {
        FloatingHelper.openAndStart(this);
    }

    public void onOpenAccessibilitySetting(View view) {
        if (!AccessibilitySettingsHelper.isEnabled(this)) {
            AccessibilitySettingsHelper.open(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SysSettingsContant.FLOATING_CODE:
                FloatingHelper.resultAndStart(this);
                break;
            case ACCESSIBILITY_CODE:
                Toast.makeText(this, "ACCESSIBILITY 授权反馈:" + resultCode, Toast.LENGTH_SHORT).show();
                System.out.println(data);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(floatingIntent);
    }
}