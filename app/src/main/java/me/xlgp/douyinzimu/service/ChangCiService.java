package me.xlgp.douyinzimu.service;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import me.xlgp.douyinzimu.dao.ChangCiDao;
import me.xlgp.douyinzimu.db.AppDatabase;
import me.xlgp.douyinzimu.designpatterns.ObserverHelper;
import me.xlgp.douyinzimu.model.ChangCi;

public class ChangCiService {

    private final CompositeDisposable compositeDisposable;

    public ChangCiService(CompositeDisposable compositeDisposable) {
        this.compositeDisposable = compositeDisposable;
    }

    public ChangCiService() {
        this.compositeDisposable = new CompositeDisposable();
    }

    public void listByChangDuanId(int id, Consumer<List<ChangCi>> consumer) {
        ChangCiDao changCiDao = AppDatabase.getInstance().changCiDao();
        Disposable disposable = changCiDao.listByChangDuanId(id)
                .compose(ObserverHelper.flowableTransformer())
                .subscribe(consumer);
        compositeDisposable.add(disposable);
    }

    public @NonNull Flowable<List<ChangCi>> list() {
        ChangCiDao changCiDao = AppDatabase.getInstance().changCiDao();
        return changCiDao.list().compose(ObserverHelper.flowableTransformer());
    }

    public void deleteByChangDuanId(int id) {
        listByChangDuanId(id, this::deleteList);
    }

    public @NonNull Single<Integer> deleteList(List<ChangCi> list) {
        ChangCiDao changCiDao = AppDatabase.getInstance().changCiDao();
        return changCiDao.deleteAll(list).compose(ObserverHelper.singleTransformer());
    }

    public @NonNull Flowable<Object> deleteAll() {
        return list().flatMapSingle(this::deleteList);
    }
}
