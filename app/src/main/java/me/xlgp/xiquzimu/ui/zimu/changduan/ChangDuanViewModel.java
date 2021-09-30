package me.xlgp.xiquzimu.ui.zimu.changduan;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import me.xlgp.xiquzimu.model.ChangDuan;
import me.xlgp.xiquzimu.data.ChangDuanRepository;
import me.xlgp.xiquzimu.util.PinYinHelper;

public class ChangDuanViewModel extends ViewModel {

    private MutableLiveData<List<ChangDuan>> changDuanList;
    MutableLiveData<String> state = new MutableLiveData<>();
    private String juZhong = "";
    MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    public MutableLiveData<List<ChangDuan>> getChangDuanList() {
        if (changDuanList == null){
            changDuanList = new MutableLiveData<>();
            loadChangDuan(juZhong);
        }
        return changDuanList;
    }

    public void setJuZhong(String juZhong) {
        this.juZhong = juZhong;
    }

    public void loadChangDuan(String juZhong) {
        ChangDuanRepository changDuanRepository = new ChangDuanRepository();
        loading.setValue(true);
        changDuanRepository.list(juZhong).subscribe(list -> {
            loading.setValue(false);
            if (list == null || list.size() == 0) {
                state.setValue("无数据可更新");
            } else {
                PinYinHelper.sortByPinYin(list);
            }
            changDuanList.setValue(list);
        }, throwable -> {
            loading.setValue(false);
            state.setValue("获取唱段异常");
        });
    }
}