package me.xlgp.douyinzimu.service;

import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.LifecycleService;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import me.xlgp.douyinzimu.ZimuApplication;
import me.xlgp.douyinzimu.obj.ZWindowManager;
import me.xlgp.douyinzimu.util.FloatingHelper;
import me.xlgp.douyinzimu.view.ZimuMainFloatingLayout;

public class FloatingService extends LifecycleService {
    private String floatingLayout = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (FloatingHelper.enable(this.getApplicationContext()) && !ZWindowManager.getInstance().containView(floatingLayout)) {
            floatingLayout = new ZimuMainFloatingLayout(this).getLayoutName();
        } else {
            Toast.makeText(this, "已启动悬浮窗", Toast.LENGTH_SHORT).show();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void stop() {
        ZWindowManager zWindowManager = ZWindowManager.getInstance();
        if (zWindowManager.count() == 0) {
            stopSelf();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ZWindowManager zWindowManager = ZWindowManager.getInstance();
        zWindowManager.removeAllView();
        CompositeDisposable compositeDisposable = ZimuApplication.getCompositeDisposable();
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }
}
