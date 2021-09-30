package me.xlgp.xiquzimu.ui.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<Result<QuanxianState>> floatQuanxianState;
    private final MutableLiveData<Result<QuanxianState>> accessibilitySettingStatus;

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