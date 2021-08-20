package me.xlgp.douyinzimu.service;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import me.xlgp.douyinzimu.dao.ChangDuanDao;
import me.xlgp.douyinzimu.db.AppDatabase;
import me.xlgp.douyinzimu.designpatterns.ObserverHelper;
import me.xlgp.douyinzimu.model.ChangDuan;
import me.xlgp.douyinzimu.obj.Callback;
import me.xlgp.douyinzimu.obj.changduan.ChangCiList;
import me.xlgp.douyinzimu.obj.changduan.ChangDuanInfo;
import me.xlgp.douyinzimu.util.ChangDuanHelper;
import me.xlgp.douyinzimu.util.HttpURLConnectionUtil;

public class ChangDuanService {

    private final CompositeDisposable compositeDisposable;

    public ChangDuanService(CompositeDisposable compositeDisposable) {
        this.compositeDisposable = compositeDisposable;
    }

    public void list(Consumer<List<ChangDuan>> consumer) {
        ChangDuanDao changDuanDao = AppDatabase.getInstance().changDuanDao();
        Disposable disposable = changDuanDao.list().compose(ObserverHelper.flowableTransformer()).subscribe(consumer);
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
                }).compose(ObserverHelper.transformer()).subscribe(nextConsumer == null ? (Consumer<Object>) o -> {
                } : nextConsumer, errorConsumer);
        compositeDisposable.add(disposable);
    }

    public void update(String name, Callback<Throwable> callback) {
        String httpBaseUrl = "https://gitee.com/xlgp/opera-lyrics/raw/master";
        HttpURLConnectionUtil.asyncGet(httpBaseUrl + name.substring(1), list -> save(ChangDuanHelper.parse(list), null, callback::call));
    }

    public void updateList(List<String> nameList) {
        for (String name : nameList) {
            update(name, Throwable::printStackTrace);
        }
    }

    public void delete(ChangDuan data, Consumer<Object> consumer) {
        ChangDuanDao changDuanDao = AppDatabase.getInstance().changDuanDao();
        Disposable disposable = Observable.create(emitter -> {
            changDuanDao.delete(data);
            new ChangCiService(compositeDisposable).deleteByChangDuanId(data.getId());
            emitter.onNext(true);
        }).compose(ObserverHelper.transformer()).subscribe(consumer);
        compositeDisposable.add(disposable);
    }

    public void deleteAll() {
        ChangDuanDao changDuanDao = AppDatabase.getInstance().changDuanDao();
        list(list -> {
            Disposable disposable = changDuanDao.deleteAll(list).compose(ObserverHelper.singleTransformer()).subscribe((integer, throwable) -> new ChangCiService(compositeDisposable).deleteAll());
            compositeDisposable.add(disposable);
        });
    }
}
