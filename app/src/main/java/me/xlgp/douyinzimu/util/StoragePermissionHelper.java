package me.xlgp.douyinzimu.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

public class StoragePermissionHelper {
    public static void request(Activity activity){
        String permissions[] = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        activity.requestPermissions(permissions, 0);
    }
    public static Boolean check(Context context){
        return context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
}
