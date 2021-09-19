package me.xlgp.douyinzimu.ui.dashboard;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.promeg.pinyinhelper.Pinyin;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import me.xlgp.douyinzimu.designpatterns.ObserverHelper;
import me.xlgp.douyinzimu.model.ChangDuan;
import me.xlgp.douyinzimu.data.ChangCiRepository;
import me.xlgp.douyinzimu.data.ChangDuanRepository;
import me.xlgp.douyinzimu.data.FetchGiteeRepository;
import me.xlgp.douyinzimu.util.ChangDuanHelper;

public class ChangDuanViewModel extends ViewModel {

    public MutableLiveData<String> deleteState = new MutableLiveData<>();
    MutableLiveData<List<ChangDuan>> changduanList = null;

    public MutableLiveData<List<ChangDuan>> getChangduanList() {
        if (changduanList == null) {
            changduanList = new MutableLiveData<>();
            loadChangDuanList();
        }
        return changduanList;
    }

    public void loadChangDuanList() {
        new ChangDuanRepository().list().subscribe(changDuanList -> {
            changDuanList.sort(Comparator.comparing(o -> Pinyin.toPinyin(o.getJuMu().charAt(0))));
            changduanList.postValue(changDuanList);
        }, throwable -> changduanList.postValue(new ArrayList<>()));
    }

    public Observable<Long> fetchChangDuanList() {
        ChangDuanRepository changDuanRepository = new ChangDuanRepository();
        return changDuanRepository.updateList()
                .flatMap((Function<List<String>, ObservableSource<String>>) list -> {
                    if (list.size() == 0) throw new NoSuchElementException("没有远程数据");
                    return Observable.fromIterable(list);
                }).flatMap((Function<String, ObservableSource<List<String>>>) s -> new FetchGiteeRepository().changDuan(s.substring(1)))
                .flatMap((Function<List<String>, ObservableSource<Long>>) list -> changDuanRepository.saveAynsc(ChangDuanHelper.parse(list)))
                .compose(ObserverHelper.transformer());
    }

    public void deleteChangDuanList() {

        Observable.concat(observer -> new ChangDuanRepository().deleteAll(),
                observer -> new ChangCiRepository().deleteAll())
                .flatMap(observableSource -> null)
                .compose(ObserverHelper.transformer())
                .subscribe(o -> deleteState.setValue("删除成功"), throwable -> deleteState.setValue("删除失败"));
    }
}
