package me.xlgp.douyinzimu.ui.about;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import me.xlgp.douyinzimu.BuildConfig;

public class AboutViewModel extends ViewModel {

    private final MutableLiveData<String> packageVersion = new MutableLiveData<>();

    public MutableLiveData<String> getPackageVersion() {
        packageVersion.setValue("版本号：" + BuildConfig.VERSION_NAME);
        return packageVersion;
    }
}
