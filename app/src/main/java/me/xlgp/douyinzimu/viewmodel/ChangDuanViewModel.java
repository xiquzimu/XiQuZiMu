package me.xlgp.douyinzimu.viewmodel;

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
import me.xlgp.douyinzimu.service.ChangCiService;
import me.xlgp.douyinzimu.service.ChangDuanService;
import me.xlgp.douyinzimu.service.FetchGiteeService;
import me.xlgp.douyinzimu.util.ChangDuanHelper;

public class ChangDuanViewModel extends ViewModel {

    MutableLiveData<List<ChangDuan>> changduanList = null;
    public MutableLiveData<String> deleteState = new MutableLiveData<>();


    public MutableLiveData<List<ChangDuan>> getChangduanList() {
        if (changduanList == null) {
            changduanList = new MutableLiveData<>();
            loadChangDuanList();
        }
        return changduanList;
    }

    public void loadChangDuanList() {
        new ChangDuanService().list().subscribe(changDuanList -> {
            changDuanList.sort(Comparator.comparing(o -> Pinyin.toPinyin(o.getJuMu().charAt(0))));
            changduanList.postValue(changDuanList);
        }, throwable -> changduanList.postValue(new ArrayList<>()));
    }

    public Observable<Long> fetchChangDuanList() {
        ChangDuanService changDuanService = new ChangDuanService();
        return changDuanService.updateList()
                .flatMap((Function<List<String>, ObservableSource<String>>) list -> {
                    if (list.size() == 0) throw new NoSuchElementException("没有远程数据");
                    return Observable.fromIterable(list);
                }).flatMap((Function<String, ObservableSource<List<String>>>) s -> new FetchGiteeService().changDuan(s.substring(1)))
                .flatMap((Function<List<String>, ObservableSource<Long>>) list -> changDuanService.saveAynsc(ChangDuanHelper.parse(list)))
                .compose(ObserverHelper.transformer());
    }

    public void deleteChangDuanList() {

        Observable.concat(observer -> new ChangDuanService().deleteAll(),
                observer -> new ChangCiService().deleteAll())
                .flatMap(observableSource -> null)
                .compose(ObserverHelper.transformer())
                .subscribe(o -> deleteState.setValue("删除成功"), throwable -> deleteState.setValue("删除失败"));
    }
}
