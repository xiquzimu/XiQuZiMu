package me.xlgp.douyinzimu.db;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;
import me.xlgp.douyinzimu.constant.AppConstant;

public class LocalSqlite {
    private String DB_PATH = "/data/data/me.xlgp.douyinzimu/databases/";

    public void cloneDataAsync(AssetManager assetManager){
        Observable.create(emitter -> cloneData(assetManager)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    public void cloneData(AssetManager assetManager) {
        File file = new File(DB_PATH + AppConstant.SQLITE_DB_NAME);
        if (!file.exists()) {
            File dir = new File(DB_PATH);
            if (!dir.exists()) {
                dir.mkdir();
            }

            InputStream is = null;
            OutputStream os = null;
            try {
                // 得到 assets 目录下我们实现准备好的 SQLite 数据库作为输入流
                is = assetManager.open(AppConstant.SQLITE_DB_NAME);
                // 输出流
                os = new FileOutputStream(DB_PATH + AppConstant.SQLITE_DB_NAME);


                // 文件写入
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }


                // 关闭文件流
                os.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (os != null) os.close();
                    if (is != null) is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
