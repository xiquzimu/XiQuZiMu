package me.xlgp.xiquzimu.service;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleService;

import org.jetbrains.annotations.NotNull;

import me.xlgp.xiquzimu.util.APKHelper;

public class DownloadService extends LifecycleService {

    private boolean downloading = false;
    private String downloadUrl = null;
    private String apkName = null;
    private BroadcastReceiver receiver;

    public static String KEY_DOWNLOAD_URL = "apkDownloadUrl";

    @Nullable
    @Override
    public IBinder onBind(@NotNull Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (downloading) {
            Toast.makeText(this, "正在下载,请勿重复操作...", Toast.LENGTH_SHORT).show();
            return super.onStartCommand(intent, flags, startId);
        }

        downloadUrl = intent.getStringExtra(KEY_DOWNLOAD_URL);
        apkName = APKHelper.getApkName(downloadUrl);

        if (!APKHelper.checkVersion(apkName)) {
            Toast.makeText(this, "无版本可更新", Toast.LENGTH_SHORT).show();
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        receiver = new DownloadBroadcastReceiver();

        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        startDownload();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        downloading = false;
        super.onDestroy();
    }

    private void startDownload() {
        APKHelper.download(this.getApplicationContext(), downloadUrl);
        downloading = true;
        Toast.makeText(this, "正在下载", Toast.LENGTH_SHORT).show();
    }

    class DownloadBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            APKHelper.installAPK(context.getApplicationContext(), apkName);
            stopSelf();
        }
    }
}
