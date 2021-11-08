package me.xlgp.xiquzimu.service;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleService;

import org.jetbrains.annotations.NotNull;

import me.xlgp.xiquzimu.BuildConfig;
import me.xlgp.xiquzimu.R;
import me.xlgp.xiquzimu.util.InstallAPKUtil;

public class DownloadService extends LifecycleService {

    private boolean downloading = false;
    private String downloadUrl = null;
    private String apkName = null;
    private BroadcastReceiver receiver;

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

        downloadUrl = intent.getStringExtra("apkDownloadUrl");
        apkName = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1);

        if (!checkVersion(apkName)) {
            Toast.makeText(this, "无版本可更新", Toast.LENGTH_SHORT).show();
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        receiver = new DownloadBroadcastReceiver();

        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        startDownload();

        return super.onStartCommand(intent, flags, startId);
    }

    private boolean checkVersion(String apkName) {
        String[] list = apkName.split("_");
        if (list.length <= 1) {
            return false;
        }
        String version = list[1].substring(1);
        return BuildConfig.VERSION_NAME.compareTo(version) < 0;
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
        Uri mApkUri = Uri.parse("https://gitee.com" + downloadUrl);

        DownloadManager.Request request = new DownloadManager.Request(mApkUri);

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setTitle(getString(R.string.app_zh_name));
        request.setDescription("在正下载 " + apkName + " ...");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, apkName);

        DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        dm.enqueue(request);
        downloading = true;
        Toast.makeText(this, "正在下载", Toast.LENGTH_SHORT).show();
    }

    class DownloadBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            InstallAPKUtil.installAPK(context.getApplicationContext(), apkName);
            stopSelf();
        }
    }
}
