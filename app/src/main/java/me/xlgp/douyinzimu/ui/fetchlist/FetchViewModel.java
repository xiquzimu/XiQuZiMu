package me.xlgp.douyinzimu.ui.fetchlist;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import me.xlgp.douyinzimu.designpatterns.ObserverHelper;
import me.xlgp.douyinzimu.data.FetchGiteeRepository;

public class FetchViewModel extends ViewModel {

    MutableLiveData<List<String>> nameList;

    public FetchViewModel() {

    }

    public MutableLiveData<List<String>> getNameList() {
        if (nameList == null) {
            nameList = new MutableLiveData<>();
            fetchNameList();
        }
        return nameList;
    }

    public void fetchNameList() {
        new FetchGiteeRepository().getNameList().compose(ObserverHelper.transformer())
                .subscribe(list -> nameList.postValue(list), throwable -> nameList.setValue(new ArrayList<>()));
    }
}