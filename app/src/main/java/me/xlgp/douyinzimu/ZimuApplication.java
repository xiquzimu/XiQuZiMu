package me.xlgp.douyinzimu;

import android.app.Application;
import android.view.WindowManager;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import me.xlgp.douyinzimu.db.AppDatabase;
import me.xlgp.douyinzimu.obj.ZWindowManager;

public class ZimuApplication extends Application {

    private static CompositeDisposable compositeDisposable;

    public static CompositeDisposable getCompositeDisposable() {
        if (compositeDisposable == null) {
            synchronized (ZimuApplication.class) {
                if (compositeDisposable == null) {
                    compositeDisposable = new CompositeDisposable();
                }
            }
        }
        return compositeDisposable;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppDatabase.build(getApplicationContext());
        ZWindowManager.getInstance().build((WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE));
    }

}
