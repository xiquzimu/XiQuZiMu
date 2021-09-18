package me.xlgp.douyinzimu.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import me.xlgp.douyinzimu.model.ChangCi;

@Dao
public interface ChangCiDao extends IDao<ChangCi> {

    @Query("select * from changci where cd_id = :changDuanId")
    Flowable<List<ChangCi>> listByChangDuanId(long changDuanId);

    @Query("select * from changci")
    List<ChangCi> list();

    @Query("delete from changci")
    void deleteAll();
}
