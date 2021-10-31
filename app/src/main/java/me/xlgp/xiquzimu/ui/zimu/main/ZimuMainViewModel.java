package me.xlgp.xiquzimu.ui.zimu.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import me.xlgp.xiquzimu.constant.JuZhongConstant;

public class ZimuMainViewModel extends ViewModel {

    private MutableLiveData<List<String>> tabNameList;

    public MutableLiveData<List<String>> getTabNameList() {
        if (tabNameList == null) {
            tabNameList = new MutableLiveData<>();
            initTabNameList();
        }
        return tabNameList;
    }

    private void initTabNameList() {
        List<String> list = JuZhongConstant.list();
        tabNameList.postValue(list);
    }
}
