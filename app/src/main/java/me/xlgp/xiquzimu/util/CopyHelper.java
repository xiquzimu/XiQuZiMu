package me.xlgp.xiquzimu.util;

import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class CopyHelper {
    public static void copy(ClipData clipData, Application application) {
        ClipboardManager clipboardManager = (ClipboardManager) application.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(clipData);
    }
}
