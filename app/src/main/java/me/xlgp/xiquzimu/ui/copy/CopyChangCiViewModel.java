package me.xlgp.xiquzimu.ui.copy;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import me.xlgp.xiquzimu.data.ChangDuanInfoRepository;
import me.xlgp.xiquzimu.model.ChangDuanInfo;

public class CopyChangCiViewModel extends AndroidViewModel {

    private MutableLiveData<ChangDuanInfo> changDuanInfoLiveData;
    public MutableLiveData<String> state = new MutableLiveData<>();

    public CopyChangCiViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public MutableLiveData<ChangDuanInfo> getChangDuanInfo() {
        if (changDuanInfoLiveData == null) {
            changDuanInfoLiveData = new MutableLiveData<>();
        }
        return changDuanInfoLiveData;
    }

    public void loadData(int id) {
        if (id < 0) {
            state.postValue("唱段ID : " + id + " 不正确");
            return;
        }
        new ChangDuanInfoRepository().getChangDuanInfo(id).subscribe(new Observer<ChangDuanInfo>() {
            private Disposable disposable;

            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull ChangDuanInfo changDuanInfo) {
                changDuanInfoLiveData.postValue(changDuanInfo);
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                state.postValue("获取唱段失败；" + e.getMessage());
                if (disposable != null && !disposable.isDisposed()) {
                    disposable.isDisposed();
                }
            }

            @Override
            public void onComplete() {
                if (disposable != null && !disposable.isDisposed()) {
                    disposable.isDisposed();
                }
            }
        });
    }
}
