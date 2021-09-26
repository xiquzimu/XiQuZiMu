package me.xlgp.douyinzimu.ui.zimu.changci;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Observer;

import me.xlgp.douyinzimu.data.ChangCiRepository;
import me.xlgp.douyinzimu.model.ChangDuan;
import me.xlgp.douyinzimu.model.ChangCiList;
import me.xlgp.douyinzimu.model.ChangDuanInfo;
import me.xlgp.douyinzimu.util.ChangDuanHelper;

public class ChangCiViewModel extends ViewModel {

    public MutableLiveData<String> changDuanState = new MutableLiveData<>(null);
    MutableLiveData<ChangDuanInfo> changDuanInfo;

    public MutableLiveData<ChangDuanInfo> getChangDuanInfo() {
        if (changDuanInfo == null) {
            changDuanInfo = new MutableLiveData<>();
        }
        return changDuanInfo;
    }

    public void loadData(ChangDuan changDuan, Observer observer) {
        ChangCiRepository changCiRepository = new ChangCiRepository();
        changCiRepository.listByChangDuanId(changDuan.getId()).subscribe(list -> {
            ChangDuanInfo info = new ChangDuanInfo();
            if (list != null && !list.isEmpty()) {
                ChangCiList changCiList = ChangDuanHelper.parseChangCiList(changDuan, list);
                changCiList.observe(observer);
                info.setChangCiList(changCiList);
                info.setChangDuan(changDuan);
                changDuanInfo.postValue(info);
            } else {
                changDuanState.postValue("唱词为空");
            }
        }, e -> {
            changDuanState.postValue("获取唱词异常");
        });
    }
}