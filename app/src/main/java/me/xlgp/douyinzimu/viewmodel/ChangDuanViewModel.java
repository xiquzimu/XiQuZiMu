package me.xlgp.douyinzimu.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.promeg.pinyinhelper.Pinyin;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import me.xlgp.douyinzimu.designpatterns.ObserverHelper;
import me.xlgp.douyinzimu.model.ChangDuan;
import me.xlgp.douyinzimu.retrofit.RetrofitFactory;
import me.xlgp.douyinzimu.service.ChangDuanService;
import me.xlgp.douyinzimu.service.GiteeService;
import me.xlgp.douyinzimu.util.ChangDuanHelper;

public class ChangDuanViewModel extends ViewModel {

    MutableLiveData<List<ChangDuan>> changduanList = null;

    public MutableLiveData<List<ChangDuan>> getChangduanList() {
        if (changduanList == null) {
            changduanList = new MutableLiveData<>();
            loadChangDuanList();
        }
        return changduanList;
    }

    public void loadChangDuanList() {
        new ChangDuanService().list(changDuanList -> {
            changDuanList.sort(Comparator.comparing(o -> Pinyin.toPinyin(o.getJuMu().charAt(0))));
            changduanList.setValue(changDuanList);
        });
    }

    public Observable<Long> fetchChangDuanList() {
        ChangDuanService changDuanService = new ChangDuanService();
        return changDuanService.updateList()
                .flatMap((Function<List<String>, ObservableSource<String>>) list -> {
                    if (list.size() == 0) throw new NoSuchElementException("没有远程数据");
                    return Observable.fromIterable(list);
                }).flatMap((Function<String, ObservableSource<List<String>>>) s -> RetrofitFactory.get(GiteeService.class).changDuan(s.substring(1)))
                .flatMap((Function<List<String>, ObservableSource<Long>>) list -> changDuanService.saveAynsc(ChangDuanHelper.parse(list)))
                .compose(ObserverHelper.transformer());
    }
}
