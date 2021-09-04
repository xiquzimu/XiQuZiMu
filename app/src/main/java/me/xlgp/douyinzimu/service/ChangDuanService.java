package me.xlgp.douyinzimu.service;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import me.xlgp.douyinzimu.dao.ChangDuanDao;
import me.xlgp.douyinzimu.db.AppDatabase;
import me.xlgp.douyinzimu.designpatterns.ObserverHelper;
import me.xlgp.douyinzimu.model.ChangDuan;
import me.xlgp.douyinzimu.obj.Callback;
import me.xlgp.douyinzimu.obj.changduan.ChangCiList;
import me.xlgp.douyinzimu.obj.changduan.ChangDuanInfo;
import me.xlgp.douyinzimu.util.ChangDuanHelper;

public class ChangDuanService {

    private final CompositeDisposable compositeDisposable;

    public ChangDuanService(CompositeDisposable compositeDisposable) {
        this.compositeDisposable = compositeDisposable;
    }
    public ChangDuanService(){
        this.compositeDisposable = new CompositeDisposable();
    }

    public void list(Consumer<List<ChangDuan>> consumer) {
        ChangDuanDao changDuanDao = AppDatabase.getInstance().changDuanDao();
        Disposable disposable = changDuanDao.list().compose(ObserverHelper.flowableTransformer()).subscribe(consumer);
        compositeDisposable.add(disposable);
    }

    public long save(ChangDuanInfo changDuanInfo){
        AppDatabase db = AppDatabase.getInstance();
        long id = db.changDuanDao().insert(changDuanInfo.getChangDuan());
        ChangCiList changCiList = changDuanInfo.getChangeCiList();
        for (int i = 0; i < changCiList.size(); i++) {
            changCiList.get(i).setChangDuanId(id);
        }
        db.changCiDao().insert(changCiList);
        return id;
    }

    public void saveAynsc(ChangDuanInfo changDuanInfo, Consumer<Throwable> errorConsumer){
        Disposable disposable =Observable.just(changDuanInfo)
                .filter(changDuanInfo12 -> changDuanInfo12.getChangDuan() != null && changDuanInfo12.getChangeCiList().size() > 0).map(this::save).compose(ObserverHelper.transformer()).subscribe((Consumer<Object>) o -> {
                }, errorConsumer);
        compositeDisposable.add(disposable);
    }

    public void update(String name, Callback<Throwable> callback) {
        GiteeService giteeService = RetrofitFactory.get(GiteeService.class);
        Disposable disposable = giteeService.changDuan(name.substring(1)).compose(ObserverHelper.transformer()).subscribe(list -> saveAynsc(ChangDuanHelper.parse(list), callback::call));
        compositeDisposable.add(disposable);
    }

    public @NonNull Observable<Object> updateList(List<String> nameList) {
        GiteeService giteeService = RetrofitFactory.get(GiteeService.class);
        return Observable.fromIterable(nameList).map((Function<String, Object>) s -> {
            giteeService.changDuan(s.substring(1)).compose(ObserverHelper.transformer()).subscribe(list -> saveAynsc(ChangDuanHelper.parse(list), Throwable::printStackTrace));
            return s;
        }).compose(ObserverHelper.transformer());
    }
    public Observable<List<String>> updateList() {
        return new FetchGiteeService().getNameList();

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
