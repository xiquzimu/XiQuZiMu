package me.xlgp.douyinzimu.ui.floating;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ZimuListViewModel extends ViewModel {

    public MutableLiveData<String> state = new MutableLiveData<>("starting");
}
