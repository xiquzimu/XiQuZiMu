package me.xlgp.douyinzimu.ui.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<Result<QuanxianState>> floatQuanxianState;
    private MutableLiveData<Result<QuanxianState>> accessibilitySettingStatus;

    public HomeViewModel() {
        floatQuanxianState = new MutableLiveData<>();
        accessibilitySettingStatus = new MutableLiveData<>();
    }

    public MutableLiveData<Result<QuanxianState>> getFloatQuanxianState() {
        return floatQuanxianState;
    }

    public MutableLiveData<Result<QuanxianState>> getAccessibilitySettingStatus() {
        return accessibilitySettingStatus;
    }
}