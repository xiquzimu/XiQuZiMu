package me.xlgp.douyinzimu.service;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import me.xlgp.douyinzimu.dao.ChangCiDao;
import me.xlgp.douyinzimu.db.AppDatabase;
import me.xlgp.douyinzimu.model.ChangCi;

public class ChangCiService {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void listByChangDuanId(int id, Consumer<List<ChangCi>> consumer) {
        ChangCiDao changCiDao = AppDatabase.getInstance().changCiDao();
        Disposable disposable = changCiDao.listByChangDuanId(id)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
        compositeDisposable.add(disposable);
    }

    public @NonNull Flowable<List<ChangCi>> list() {
        ChangCiDao changCiDao = AppDatabase.getInstance().changCiDao();
        return changCiDao.list().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public void dispose() {
        if (!compositeDisposable.isDisposed()) compositeDisposable.dispose();
    }

    public void deleteByChangDuanId(int id) {
        listByChangDuanId(id, this::deleteList);
    }

    public void deleteList(List<ChangCi> list) {
        ChangCiDao changCiDao = AppDatabase.getInstance().changCiDao();
        Disposable disposable = Observable.just(list).subscribe(changCis -> {
            Disposable disposable1 = changCiDao.deleteAll(changCis).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
            compositeDisposable.add(disposable1);
        });
        compositeDisposable.add(disposable);
    }

    public void deleteAll() {
        Disposable disposable = list().subscribe(this::deleteList);
        compositeDisposable.add(disposable);
    }
}
