package me.xlgp.xiquzimu.ui.dashboard;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import me.xlgp.xiquzimu.data.ChangCiRepository;
import me.xlgp.xiquzimu.data.ChangDuanRepository;
import me.xlgp.xiquzimu.data.FetchRemoteRepository;
import me.xlgp.xiquzimu.designpatterns.ObserverHelper;
import me.xlgp.xiquzimu.params.TagChangDuan;
import me.xlgp.xiquzimu.util.ChangDuanHelper;
import me.xlgp.xiquzimu.util.PinYinHelper;

public class ChangDuanViewModel extends ViewModel {

    public MutableLiveData<String> deleteState = new MutableLiveData<>();
    public MutableLiveData<String> loadState = new MutableLiveData<>();
    MutableLiveData<List<TagChangDuan>> changduanList = null;

    public MutableLiveData<List<TagChangDuan>> getChangduanList() {
        if (changduanList == null) {
            changduanList = new MutableLiveData<>();
            loadChangDuanList();
        }
        return changduanList;
    }


    public void loadChangDuanList() {
        new ChangDuanRepository().list().subscribe(changDuanList -> {
            PinYinHelper.sortByPYAndName(changDuanList);
            List<TagChangDuan> list = ChangDuanHelper.getTagChangDuan(changDuanList);
            changduanList.postValue(list);
        }, throwable -> {
            changduanList.postValue(new ArrayList<>());
            loadState.postValue("获取失败" + throwable.getMessage());
        });
    }

    public Observable<Long> fetchChangDuanList() {
        ChangDuanRepository changDuanRepository = new ChangDuanRepository();
        FetchRemoteRepository fetchRemoteRepository = new FetchRemoteRepository();
        return changDuanRepository.updateList()
                .flatMap((Function<List<String>, ObservableSource<String>>) list -> {
                    if (list.size() == 0) throw new NoSuchElementException("没有远程数据");
                    return Observable.fromIterable(list);
                }).flatMap((Function<String, ObservableSource<List<String>>>) s -> fetchRemoteRepository.changDuan(s.substring(1)))
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
