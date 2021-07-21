package me.xlgp.douyinzimu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import me.xlgp.douyinzimu.service.FloatingService;
import me.xlgp.douyinzimu.util.AccessibilitySettingsHelper;
import me.xlgp.douyinzimu.util.CopyUtil;

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
        if (Settings.canDrawOverlays(this)) {
            startFloatingService();
        } else {
            Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT);
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), FLOATING_CODE);
        }
    }

    public void startFloatingService() {
        floatingIntent = new Intent(MainActivity.this, FloatingService.class);
        startService(floatingIntent);
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
            case FLOATING_CODE:
                if (!Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                    startFloatingService();
                }
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