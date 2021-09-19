package me.xlgp.douyinzimu.ui.zimu.changduan;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import me.xlgp.douyinzimu.model.ChangDuan;
import me.xlgp.douyinzimu.service.ChangDuanService;
import me.xlgp.douyinzimu.util.PinYinHelper;

public class ChangDuanViewModel extends ViewModel {

    private MutableLiveData<List<ChangDuan>> changDuanList;
    MutableLiveData<String> state = new MutableLiveData<>();
    MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    public MutableLiveData<List<ChangDuan>> getChangDuanList() {
        if (changDuanList == null){
            changDuanList = new MutableLiveData<>();
            loadChangDuan();
        }
        return changDuanList;
    }

    public void loadChangDuan() {
        ChangDuanService changDuanService = new ChangDuanService();
        loading.setValue(true);
        changDuanService.list().subscribe(list -> {
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