package me.xlgp.douyinzimu.viewmodel;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.promeg.pinyinhelper.Pinyin;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import me.xlgp.douyinzimu.model.ChangDuan;
import me.xlgp.douyinzimu.service.ChangDuanService;
import me.xlgp.douyinzimu.service.FetchGiteeService;

public class ChangDuanViewModel extends ViewModel {

    MutableLiveData<List<ChangDuan>> changduanList = null;

    public MutableLiveData<List<ChangDuan>> getChangduanList() {
        if (changduanList == null){
            changduanList = new MutableLiveData<>();
            loadChangDuanList();
        }
        return changduanList;
    }
    public void loadChangDuanList(){
        new ChangDuanService().list(changDuanList -> {
            changDuanList.sort(Comparator.comparing(o -> Pinyin.toPinyin(o.getJuMu().charAt(0))));
            changduanList.setValue(changDuanList);
        });
    }
    public void fetchChangDuanList(){
        ChangDuanService changDuanService = new ChangDuanService();
        changDuanService.updateList().subscribe(list -> {
            list = list.stream().filter(s -> s.endsWith(".lrc")).collect(Collectors.toList());
            Observable<Object> observable = changDuanService.updateList(list);
            observable.subscribe(o -> Log.i("TAG", "accept: " + o.toString()));
        });
    }
}
