package me.xlgp.douyinzimu.ui.zimu;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import me.xlgp.douyinzimu.model.ChangDuan;

public class ZimuViewModel extends ViewModel {

    private static MutableLiveData<ChangDuan> changDuan;

    public static MutableLiveData<ChangDuan> getChangDuan(){
        if (changDuan == null){
            synchronized (ZimuViewModel.class){
                if (changDuan == null){
                    changDuan = new MutableLiveData<>();
                }
            }
        }
        return changDuan;
    }
}
