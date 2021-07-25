package me.xlgp.douyinzimu;

import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import me.xlgp.douyinzimu.constant.SysSettingsContant;
import me.xlgp.douyinzimu.util.AccessibilitySettingsHelper;
import me.xlgp.douyinzimu.util.CopyUtil;
import me.xlgp.douyinzimu.util.FloatingHelper;

public class MainActivity extends AppCompatActivity {
    private Intent floatingIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onStartFloatingService(View view) {
        if (!AccessibilitySettingsHelper.isEnabled(this)) {
            Toast.makeText(this, "请先开启无障碍服务", Toast.LENGTH_SHORT).show();
            return;
        }
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
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(floatingIntent);
    }
}