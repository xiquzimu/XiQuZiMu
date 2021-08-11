package me.xlgp.douyinzimu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import me.xlgp.douyinzimu.db.AppDatabase;
import me.xlgp.douyinzimu.db.LocalSqlite;
import me.xlgp.douyinzimu.util.AccessibilitySettingsHelper;
import me.xlgp.douyinzimu.util.FileHelper;
import me.xlgp.douyinzimu.util.FloatingHelper;
import me.xlgp.douyinzimu.util.StoragePermissionHelper;

public class MainActivity extends AppCompatActivity {
    private Intent floatingIntent = null;

    private Button openFloatingBtn = null;
    private Button openAccessibilitySettingBtn = null;
    private ActivityResultLauncher<Intent> floatingLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> FloatingHelper.updateFloatingBtn(MainActivity.this, openFloatingBtn));
    private ActivityResultLauncher<Intent> accessibilityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> AccessibilitySettingsHelper.updateAccessibilitySettingBtn(this, openAccessibilitySettingBtn));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openFloatingBtn = findViewById(R.id.openFloatingServiceBtn);
        openAccessibilitySettingBtn = findViewById(R.id.openAccessibilitySetting);

        AccessibilitySettingsHelper.updateAccessibilitySettingBtn(this, openAccessibilitySettingBtn);
        FloatingHelper.updateFloatingBtn(this, openFloatingBtn);

        initPackageVersion();

        new LocalSqlite().cloneDataAsync(getAssets());
    }

    private void initPackageVersion() {
        TextView textView = findViewById(R.id.packageVersionText);
        textView.setText("版本号：" + BuildConfig.VERSION_NAME);
    }

    public void onStartFloatingService(View view) {
        if (!FloatingHelper.enable(this)) {
            floatingLauncher.launch(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + MainActivity.this.getPackageName())));
            return;
        }
        Toast.makeText(this, "已开启悬浮权限", Toast.LENGTH_SHORT).show();
    }

    public void onOpenAccessibilitySetting(View view) {
        if (!AccessibilitySettingsHelper.isEnabled(this)) {
            accessibilityLauncher.launch(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            return;
        }
        Toast.makeText(this, "已开启无障碍服务", Toast.LENGTH_SHORT).show();
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
        if (!StoragePermissionHelper.check(this)) { //是否有存储文件读取权限
            StoragePermissionHelper.request(this);
            return;
        }
        floatingIntent = FloatingHelper.getFloatingIntent(this);
        Observable.create(emitter -> {
            emitter.onNext(startService(floatingIntent));
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}