package me.xlgp.xiquzimu.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;

public class InstallAPKUtil {
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
}
