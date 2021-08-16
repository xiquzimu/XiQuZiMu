package me.xlgp.douyinzimu.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import me.xlgp.douyinzimu.model.ChangDuan;

public class ChangDuanViewModel extends ViewModel {

    MutableLiveData<List<ChangDuan>> changduanList = new MutableLiveData<>();

    public MutableLiveData<List<ChangDuan>> getChangduanList() {
        return changduanList;
    }
}
