package me.xlgp.douyinzimu;

import android.app.Application;
import android.view.WindowManager;

import me.xlgp.douyinzimu.db.AppDatabase;
import me.xlgp.douyinzimu.obj.ZWindowManager;

public class ZimuApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppDatabase.build(getApplicationContext());
        ZWindowManager.getInstance().build((WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE));
    }

}
