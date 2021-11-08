package me.xlgp.xiquzimu.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;

public class AppInstallReceiver extends BroadcastReceiver {

    private final String dirType = Environment.DIRECTORY_DOWNLOADS;

    private File[] getApkFileList(Context context) {
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
        return file.listFiles(filter);
    }

    private void removeApk(Context context, File[] files) {
        for (File file : files) {
            Log.i("TAG", "removeApk: " + file.getAbsolutePath());
            Toast.makeText(context, file.getName(), Toast.LENGTH_SHORT).show();
            try {
                boolean bool = file.delete();
                if (!bool) {
                    throw new Exception("删除失败");
                }
            } catch (Exception e) {
                Log.e("TAG", "removeApk: ", e);
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        switch (action) {
            case Intent.ACTION_PACKAGE_ADDED:
            case Intent.ACTION_PACKAGE_REPLACED:
                removeApk(context, getApkFileList(context));
                break;
            default:
                break;
        }
    }
}
