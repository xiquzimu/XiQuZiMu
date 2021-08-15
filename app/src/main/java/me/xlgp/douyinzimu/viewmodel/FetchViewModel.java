package me.xlgp.douyinzimu.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class FetchViewModel extends ViewModel {

    MutableLiveData<List<String>> nameList;

    public FetchViewModel() {

    }

    public MutableLiveData<List<String>> getNameList() {
        if (nameList == null) {
            nameList = new MutableLiveData<>();
        }
        return nameList;
    }
}
