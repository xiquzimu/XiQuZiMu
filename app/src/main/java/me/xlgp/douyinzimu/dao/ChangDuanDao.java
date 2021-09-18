package me.xlgp.douyinzimu.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import me.xlgp.douyinzimu.model.ChangDuan;

@Dao
public interface ChangDuanDao extends IDao<ChangDuan> {

    @Query("select * from changduan")
    Flowable<List<ChangDuan>> flowableList();

    @Query("select * from changduan")
    List<ChangDuan> list();

    @Query("select * from changduan where id = :id")
    Single<ChangDuan> get(Integer id);

    @Query("delete from changduan")
    void deleteAll();
}
