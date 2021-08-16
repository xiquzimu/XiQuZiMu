package me.xlgp.douyinzimu.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import java.util.List;

@Dao
public interface IDao<T> {

    @Insert
    long insert(T t);

    @Insert
    void insert(List<T> list);

    @Update
    void update(T t);

    @Delete
    void delete(T t);
}
