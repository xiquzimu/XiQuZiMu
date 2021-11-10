package me.xlgp.xiquzimu.util;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FilenameFilter;

import me.xlgp.xiquzimu.BuildConfig;
import me.xlgp.xiquzimu.R;

public class APKHelper {
    public static void installAPK(Context context, String apkName) {

        String directory = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath();
        File apkFile = new File(directory + "/" + apkName);
        if (!apkFile.exists()) {
            Toast.makeText(context, "安装包文件不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //安装完成后，启动app
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", apkFile);//第二个参数要和Mainfest中<provider>内的android:authorities 保持一致
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static File[] getApkFileList(Context context) {
        final String dirType = Environment.DIRECTORY_DOWNLOADS;
        final File file = context.getExternalFilesDir(dirType);
        if (file == null) {
            throw new IllegalStateException("Failed to get external storage files directory");
        } else if (file.exists()) {
            if (!file.isDirectory()) {
                throw new IllegalStateException(file.getAbsolutePath() +
                        " already exists and is not a directory");
            }
        }
        FilenameFilter filter = (dir, name) -> name.endsWith("apk");
        File[] files = file.listFiles();
        assert files != null;
        return file.listFiles(filter);
    }

    public static void removeApk(Context context) {
        removeApk(getApkFileList(context));
    }

    private static void removeApk(File[] files) {
        for (File file : files) {
            boolean b = file.delete();
        }
    }

    //检测版本
    public static boolean checkVersion(String apkName) {
        String[] list = apkName.split("_");
        if (list.length <= 1) {
            return false;
        }
        String version = list[1].substring(1);
        return BuildConfig.VERSION_NAME.compareTo(version) < 0;
    }

    public static String getApkName(String url) {
        if ("".equals(url)) {
            return url;
        }
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public static void download(Context context, String downloadUrl) {
        Uri mApkUri = Uri.parse("https://gitee.com" + downloadUrl);
        String apkName = getApkName(downloadUrl);
        DownloadManager.Request request = new DownloadManager.Request(mApkUri);

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setTitle(context.getString(R.string.app_zh_name));
        request.setDescription("在正下载 " + apkName + " ...");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, apkName);

        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        dm.enqueue(request);
    }
}
