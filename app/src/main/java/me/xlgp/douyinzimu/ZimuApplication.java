package me.xlgp.douyinzimu;

import android.app.Application;

import me.xlgp.douyinzimu.db.AppDatabase;

public class ZimuApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppDatabase.build(getApplicationContext());
    }
}
