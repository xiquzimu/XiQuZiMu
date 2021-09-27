package me.xlgp.douyinzimu.ui.zimu.changci;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import me.xlgp.douyinzimu.model.ChangDuanInfo;

public class ChangCiViewModel extends ViewModel {

    public MutableLiveData<String> changDuanState = new MutableLiveData<>(null);
    MutableLiveData<ChangDuanInfo> changDuanInfo = new MutableLiveData<>();
}