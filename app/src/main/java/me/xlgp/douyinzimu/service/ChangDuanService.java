package me.xlgp.douyinzimu.service;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import me.xlgp.douyinzimu.dao.ChangDuanDao;
import me.xlgp.douyinzimu.db.AppDatabase;
import me.xlgp.douyinzimu.model.ChangDuan;
import me.xlgp.douyinzimu.obj.Callback;
import me.xlgp.douyinzimu.obj.changduan.ChangCiList;
import me.xlgp.douyinzimu.obj.changduan.ChangDuanInfo;
import me.xlgp.douyinzimu.util.ChangDuanHelper;
import me.xlgp.douyinzimu.util.HttpURLConnectionUtil;

public class ChangDuanService {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();


    private <T extends Object> void addDisposable(T next, Consumer<? super Object> nextConsumer, Consumer<? super Throwable> errorConsumer) {
        Disposable disposable = Observable.create(emitter -> {
            emitter.onNext(next);
        }).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(nextConsumer, errorConsumer);
        compositeDisposable.add(disposable);
    }

    public void list(Consumer<List<ChangDuan>> consumer) {
        ChangDuanDao changDuanDao = AppDatabase.getInstance().changDuanDao();
        Disposable disposable = changDuanDao.list().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(consumer);
        compositeDisposable.add(disposable);
    }

    public void save(ChangDuanInfo changDuanInfo, Consumer<Object> nextConsumer, Consumer<Throwable> errorConsumer) {

        Disposable disposable = Observable.just(changDuanInfo)
                .filter(changDuanInfo12 -> changDuanInfo12.getChangDuan() != null && changDuanInfo12.getChangeCiList().size() > 0)
                .map(changDuanInfo1 -> {
                    AppDatabase db = AppDatabase.getInstance();
                    long id = db.changDuanDao().insert(changDuanInfo1.getChangDuan());
                    ChangCiList changCiList = changDuanInfo.getChangeCiList();
                    for (int i = 0; i < changCiList.size(); i++) {
                        changCiList.get(i).setChangDuanId(id);
                    }
                    db.changCiDao().insert(changCiList);
                    return "";
                }).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(nextConsumer == null ? (Consumer<Object>) o -> {
                } : nextConsumer, errorConsumer);
        compositeDisposable.add(disposable);
    }

    public void update(String name, Callback<Throwable> callback) {
        String httpBaseUrl = "https://gitee.com/xlgp/opera-lyrics/raw/master";
        HttpURLConnectionUtil.asyncGet(httpBaseUrl + name, list -> save(ChangDuanHelper.parse(list), null, callback::call));
    }

    public void delete(ChangDuan data, Consumer<String> consumer) {

        ChangDuanDao changDuanDao = AppDatabase.getInstance().changDuanDao();

       Disposable disposable = Observable.just(data).map(d->{
           changDuanDao.delete(d);
           return "";
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(consumer);
       compositeDisposable.add(disposable);
    }
}
