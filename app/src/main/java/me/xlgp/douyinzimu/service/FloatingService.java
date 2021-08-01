package me.xlgp.douyinzimu.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import me.xlgp.douyinzimu.obj.ZWindowManager;
import me.xlgp.douyinzimu.util.FloatingHelper;
import me.xlgp.douyinzimu.view.ToolFloatingLayout;

public class FloatingService extends Service {
    private String toolFloatingLayout = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (FloatingHelper.enable(this) && !ZWindowManager.getInstance(this).containView(toolFloatingLayout)) {
            toolFloatingLayout = new ToolFloatingLayout(this).getLayoutName();
        } else {
            Toast.makeText(this, "已启动悬浮窗", Toast.LENGTH_SHORT).show();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void closeFloatingWindow(View view) {
        ZWindowManager zWindowManager = ZWindowManager.getInstance(this);
        if (view == null) {
            zWindowManager.removeAllView();
            stopSelf();
        } else {
            zWindowManager.removeView(view);
        }
    }

    @Override
    public void onDestroy() {
        ZWindowManager zWindowManager = ZWindowManager.getInstance(this);
        zWindowManager.removeAllView();
    }
}
