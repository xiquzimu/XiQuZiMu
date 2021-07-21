package me.xlgp.douyinzimu.util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import java.util.Random;

public class CopyUtil {

    public static void copy(Activity activity, Object data){
        ClipboardManager clipboardManager = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(new Random().toString(), data.toString());
        clipboardManager.setPrimaryClip(clipData);
    }

    public static void copy(ClipboardManager clipboardManager, Object data){
        ClipData clipData = ClipData.newPlainText(new Random().toString(), data.toString());
        clipboardManager.setPrimaryClip(clipData);
    }

    public static String paste(ClipboardManager clipboardManager){
        ClipData clipData = clipboardManager.getPrimaryClip();
        clipData.getItemAt(0);
        if(clipData.getItemCount() > 0){
            return clipData.getItemAt(clipData.getItemCount()-1).getText().toString();
        }
        return null;
    }
}
