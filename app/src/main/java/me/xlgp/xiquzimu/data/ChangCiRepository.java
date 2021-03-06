package me.xlgp.xiquzimu.data;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import me.xlgp.xiquzimu.dao.ChangCiDao;
import me.xlgp.xiquzimu.db.AppDatabase;
import me.xlgp.xiquzimu.designpatterns.ObserverHelper;
import me.xlgp.xiquzimu.model.ChangCi;

public class ChangCiRepository {

    private final CompositeDisposable compositeDisposable;

    public ChangCiRepository(CompositeDisposable compositeDisposable) {
        this.compositeDisposable = compositeDisposable;
    }

    public ChangCiRepository() {
        this.compositeDisposable = new CompositeDisposable();
    }

    public void listByChangDuanId(int id, Consumer<List<ChangCi>> consumer) {
        ChangCiDao changCiDao = AppDatabase.getInstance().changCiDao();
        Disposable disposable = changCiDao.flowListByChangDuanId(id)
                .compose(ObserverHelper.flowableTransformer())
                .subscribe(consumer);
        compositeDisposable.add(disposable);
    }

    public @NonNull Flowable<List<ChangCi>> flowListByChangDuanId(int id) {
        ChangCiDao changCiDao = AppDatabase.getInstance().changCiDao();
        return changCiDao.flowListByChangDuanId(id)
                .compose(ObserverHelper.flowableTransformer());
    }

    public List<ChangCi> listByChangDuanId(int id) {
        ChangCiDao changCiDao = AppDatabase.getInstance().changCiDao();
        return changCiDao.listByChangDuanId(id);
    }

    public void deleteByChangDuanId(int id) {
        listByChangDuanId(id, this::deleteList);
    }

    public void deleteList(List<ChangCi> list) {
        ChangCiDao changCiDao = AppDatabase.getInstance().changCiDao();
        changCiDao.deleteAll(list).compose(ObserverHelper.singleTransformer());
    }

    public void deleteAll() {
        ChangCiDao changCiDao = AppDatabase.getInstance().changCiDao();
        changCiDao.deleteAll();
    }
}
