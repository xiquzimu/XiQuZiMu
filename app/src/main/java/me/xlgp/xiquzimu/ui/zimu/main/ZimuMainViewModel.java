package me.xlgp.xiquzimu.ui.zimu.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

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
        List<String> list = new ArrayList<>();
        list.add("黄梅戏");
        list.add("越剧");
        list.add("歌曲");
        list.add("小调");
        tabNameList.postValue(list);
    }
}
