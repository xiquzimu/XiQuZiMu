package me.xlgp.douyinzimu.data;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import me.xlgp.douyinzimu.dao.ChangDuanDao;
import me.xlgp.douyinzimu.db.AppDatabase;
import me.xlgp.douyinzimu.designpatterns.ObserverHelper;
import me.xlgp.douyinzimu.model.ChangDuan;
import me.xlgp.douyinzimu.obj.changduan.ChangCiList;
import me.xlgp.douyinzimu.obj.changduan.ChangDuanInfo;
import me.xlgp.douyinzimu.util.ChangDuanHelper;

public class ChangDuanRepository {

    private final CompositeDisposable compositeDisposable;

    public ChangDuanRepository(CompositeDisposable compositeDisposable) {
        this.compositeDisposable = compositeDisposable;
    }

    public ChangDuanRepository() {
        this.compositeDisposable = new CompositeDisposable();
    }

    public Flowable<List<ChangDuan>> list() {
        ChangDuanDao changDuanDao = AppDatabase.getInstance().changDuanDao();
        return changDuanDao.flowableList().compose(ObserverHelper.flowableTransformer());
    }

    public Flowable<List<ChangDuan>> list(String juZhong){
        ChangDuanDao changDuanDao = AppDatabase.getInstance().changDuanDao();
        return changDuanDao.flowableList(juZhong).compose(ObserverHelper.flowableTransformer());
    }

    public long save(ChangDuanInfo changDuanInfo) {
        AppDatabase db = AppDatabase.getInstance();
        long id = db.changDuanDao().insert(changDuanInfo.getChangDuan());
        ChangCiList changCiList = changDuanInfo.getChangeCiList();
        for (int i = 0; i < changCiList.size(); i++) {
            changCiList.get(i).setChangDuanId(id);
        }
        db.changCiDao().insert(changCiList);
        return id;
    }

    public @NonNull Observable<Long> saveAynsc(ChangDuanInfo changDuanInfo) {
        return Observable.just(changDuanInfo)
                .filter(changDuanInfo12 -> changDuanInfo12.getChangDuan() != null && changDuanInfo12.getChangeCiList().size() > 0).map(this::save).compose(ObserverHelper.transformer());
    }

    public @NonNull Observable<Long> update(String name) {
        return new FetchGiteeRepository().changDuan(name.substring(1))
                .flatMap((Function<List<String>, ObservableSource<Long>>) list -> saveAynsc(ChangDuanHelper.parse(list)))
                .compose(ObserverHelper.transformer());
    }

    public Observable<List<String>> updateList() {
        return new FetchGiteeRepository().getNameList();
    }

    public void delete(ChangDuan data, Consumer<Object> consumer) {
        ChangDuanDao changDuanDao = AppDatabase.getInstance().changDuanDao();
        Disposable disposable = Observable.create(emitter -> {
            changDuanDao.delete(data);
            new ChangCiRepository(compositeDisposable).deleteByChangDuanId(data.getId());
            emitter.onNext(true);
        }).compose(ObserverHelper.transformer()).subscribe(consumer);
        compositeDisposable.add(disposable);
    }

    public void deleteAll() {
        ChangDuanDao changDuanDao = AppDatabase.getInstance().changDuanDao();
        changDuanDao.deleteAll();
    }
}
