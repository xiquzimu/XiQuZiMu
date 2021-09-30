package me.xlgp.xiquzimu.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import me.xlgp.xiquzimu.model.ChangCi;

@Dao
public interface ChangCiDao extends IDao<ChangCi> {

    @Query("select * from changci where cd_id = :changDuanId")
    Flowable<List<ChangCi>> flowListByChangDuanId(long changDuanId);

    @Query("select * from changci where cd_id = :changDuanId")
    List<ChangCi> listByChangDuanId(long changDuanId);

    @Query("select * from changci")
    List<ChangCi> list();

    @Query("delete from changci")
    void deleteAll();
}
