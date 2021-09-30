package me.xlgp.xiquzimu.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Calendar;

import me.xlgp.xiquzimu.constant.AppConstant;
import me.xlgp.xiquzimu.dao.ChangCiDao;
import me.xlgp.xiquzimu.dao.ChangDuanDao;
import me.xlgp.xiquzimu.model.ChangCi;
import me.xlgp.xiquzimu.model.ChangDuan;

@Database(entities = {ChangCi.class, ChangDuan.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public static AppDatabase getInstance() {
        return instance;
    }

    static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(1970,1,1);
            database.execSQL("ALTER TABLE changduan ADD COLUMN createTime INTEGER not null default " + calendar.getTime().getTime());
        }
    };

    public static void build(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, AppConstant.SQLITE_DB_NAME)
                            .fallbackToDestructiveMigration()
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
    }

    public abstract ChangCiDao changCiDao();

    public abstract ChangDuanDao changDuanDao();
}
