package me.xlgp.xiquzimu;

import android.app.Application;
import android.content.SharedPreferences;
import android.view.WindowManager;

import me.xlgp.xiquzimu.config.FetchRepositoryConfig;
import me.xlgp.xiquzimu.db.AppDatabase;
import me.xlgp.xiquzimu.obj.ZWindowManager;

public class ZimuApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppDatabase.build(getApplicationContext());
        setFetchRepositoryConfig();
        ZWindowManager.getInstance().build((WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE));
    }

    private void setFetchRepositoryConfig() {
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(FetchRepositoryConfig.NAME, MODE_PRIVATE);
        FetchRepositoryConfig
                .setRepositoryType(sharedPreferences.
                        getInt(FetchRepositoryConfig.REPOSITORY, FetchRepositoryConfig.REPOSITORY_ENUM.GITEE.ordinal()));
    }

    public void setFetchRepositoryConfig(FetchRepositoryConfig.REPOSITORY_ENUM repositoryEnum) {
        if (FetchRepositoryConfig.containEnum(repositoryEnum)) {
            SharedPreferences.Editor editor = getApplicationContext()
                    .getSharedPreferences(FetchRepositoryConfig.NAME, MODE_PRIVATE).edit();
            editor.putInt(FetchRepositoryConfig.REPOSITORY, repositoryEnum.ordinal()).apply();
            setFetchRepositoryConfig();
        }
    }

}
