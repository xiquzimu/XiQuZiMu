package me.xlgp.xiquzimu.ui.zimu.changci;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import me.xlgp.xiquzimu.model.ChangCiList;

public class ChangCiViewModel extends ViewModel {

    public MutableLiveData<String> changDuanState = new MutableLiveData<>(null);
    MutableLiveData<ChangCiList> changCiList = new MutableLiveData<>();
}