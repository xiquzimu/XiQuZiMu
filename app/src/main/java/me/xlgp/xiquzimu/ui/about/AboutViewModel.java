package me.xlgp.xiquzimu.ui.about;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import me.xlgp.xiquzimu.BuildConfig;
import me.xlgp.xiquzimu.data.FetchGiteeRepository;
import me.xlgp.xiquzimu.designpatterns.ObserverHelper;

public class AboutViewModel extends ViewModel {

    private final MutableLiveData<String> packageVersion = new MutableLiveData<>();

    private MutableLiveData<List<String>> downloadUrlList = null;

    public MutableLiveData<String> errorState = new MutableLiveData<>();

    public MutableLiveData<String> getPackageVersion() {
        packageVersion.setValue("版本号：" + BuildConfig.VERSION_NAME);
        return packageVersion;
    }

    public MutableLiveData<List<String>> getDownloadUrlList() {
        if (downloadUrlList == null) {
            downloadUrlList = new MutableLiveData<>();
        }
        return downloadUrlList;
    }

    public void loadDownloadUrl() {
        FetchGiteeRepository fetchGiteeRepository = new FetchGiteeRepository();
        fetchGiteeRepository.getDownlaodUrl().compose(ObserverHelper.transformer()).subscribe(new Observer<List<String>>() {

            private Disposable disposable;

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(@NonNull List<String> list) {
                list.sort((o1, o2) -> -o1.substring(o1.indexOf("xiquzimu")).compareTo(o2.substring(o2.indexOf("xiquzimu"))));
                downloadUrlList.postValue(list);
                dispose();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                errorState.postValue(e.getMessage());
                dispose();
            }

            @Override
            public void onComplete() {
                dispose();
            }

            public void dispose() {
                if (disposable != null && !disposable.isDisposed()) {
                    disposable.dispose();
                }
            }
        });
    }
}
