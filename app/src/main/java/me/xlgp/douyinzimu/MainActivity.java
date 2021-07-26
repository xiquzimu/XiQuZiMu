package me.xlgp.douyinzimu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import me.xlgp.douyinzimu.util.AccessibilitySettingsHelper;
import me.xlgp.douyinzimu.util.FloatingHelper;

public class MainActivity extends AppCompatActivity {
    private Intent floatingIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onStartFloatingService(View view) {
        if (!FloatingHelper.enable(this)) {
            FloatingHelper.open(this);
            return;
        }
        Toast.makeText(this, "已开启悬浮权限", Toast.LENGTH_SHORT).show();
    }

    public void onOpenAccessibilitySetting(View view) {
        if (!AccessibilitySettingsHelper.isEnabled(this)) {
            AccessibilitySettingsHelper.open(this);
        }
    }

    public void onRun(View view) {
        if (!AccessibilitySettingsHelper.isEnabled(this)) {
            Toast.makeText(this, "请先开启无障碍服务", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!FloatingHelper.enable(this)) {
            Toast.makeText(this, "请开启悬浮权限", Toast.LENGTH_SHORT).show();
            return;
        }
        floatingIntent = FloatingHelper.getFloatingIntent(this);
        FloatingHelper.startService(this, floatingIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (floatingIntent != null) {
            stopService(floatingIntent);
        }
    }
}