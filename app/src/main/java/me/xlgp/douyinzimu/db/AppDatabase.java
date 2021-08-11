package me.xlgp.douyinzimu.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import me.xlgp.douyinzimu.constant.AppConstant;
import me.xlgp.douyinzimu.dao.ChangCiDao;
import me.xlgp.douyinzimu.dao.ChangDuanDao;
import me.xlgp.douyinzimu.model.ChangCi;
import me.xlgp.douyinzimu.model.ChangDuan;

@Database(entities = {ChangCi.class, ChangDuan.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract ChangCiDao changCiDao();
    public abstract ChangDuanDao changDuanDao();

    public static AppDatabase getInstance() {
        return instance;
    }

    public static void build(Context context){
        if (instance == null){
            synchronized (AppDatabase.class){
                if (instance == null){
                    instance = Room.databaseBuilder(context,
                            AppDatabase.class, AppConstant.SQLITE_DB_NAME).build();
                }
            }
        }
    }
}
