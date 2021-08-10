package me.xlgp.douyinzimu.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface IDao<T> {

    @Insert
    long insert(T t);

    @Insert
    void   insert(List<T> list);

    @Update
    void update(T t);
}
