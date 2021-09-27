package me.xlgp.douyinzimu.service;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import java.util.NoSuchElementException;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import me.xlgp.douyinzimu.data.ChangDuanInfoRepository;
import me.xlgp.douyinzimu.model.ChangDuanInfo;

public class PinglunLifecycleService extends LifecycleService implements ViewModelStoreOwner {

    private final String TAG = PinglunLifecycleService.class.getName();

    public static final String CHANG_DUAN_ID = "changDuanId";

    private Integer changDuanId;

    private ChangDuanInfo changDuanInfo;

    //标志能否评论
    private boolean call = false;

    private PinglunViewModel viewModel;

    public PinglunLifecycleService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: " + toString());
        viewModel = new ViewModelProvider(this).get(PinglunViewModel.class);
        viewModel.getChangDuanInfo().observe(this, changDuanInfo -> {
            this.changDuanInfo = changDuanInfo;
            call = false;
        });
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: " + flags + " " + startId + " " + toString());
        if (intent != null) {
            changDuanId = intent.getIntExtra(CHANG_DUAN_ID, -1);
            if (changDuanId > 0) {
                init();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(@NonNull Intent intent) {
        super.onBind(intent);
        return new PinglunBinder(this);
    }

    private void init() {
        if (this.changDuanInfo != null && !this.changDuanInfo.getChangDuan().getId().equals(changDuanId)) {
            this.changDuanInfo = null;
            call = false;
        }
    }

    private Observable<ChangDuanInfo> getChangDuanInfo() {
        return Observable.create(emitter -> {
            if (changDuanInfo == null)
                emitter.onError(new NoSuchElementException("没有数据"));
            else emitter.onNext(changDuanInfo);
        });
    }

    private Observable<ChangDuanInfo> initChangDuanInfo(Integer changDuanId) {
        ChangDuanInfoRepository changDuanInfoRepository = new ChangDuanInfoRepository();
        return changDuanInfoRepository.getChangDuanInfo(changDuanId).flatMap((Function<ChangDuanInfo,
                ObservableSource<ChangDuanInfo>>) changDuanInfo -> {
            viewModel.getChangDuanInfo().postValue(changDuanInfo);
            this.changDuanInfo = changDuanInfo;

            return getChangDuanInfo();
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return new ViewModelStore();
    }

    public static class PinglunViewModel extends ViewModel {
        private MutableLiveData<ChangDuanInfo> changDuanInfo;

        public MutableLiveData<ChangDuanInfo> getChangDuanInfo() {
            if (changDuanInfo == null) {
                changDuanInfo = new MutableLiveData<>();
            }
            return changDuanInfo;
        }
    }

    public static class PinglunBinder extends Binder {
        PinglunLifecycleService service = null;

        public PinglunBinder(PinglunLifecycleService service) {
            this.service = service;
        }


        public Observable<ChangDuanInfo> load() {
            if (service.changDuanInfo == null) {
                return service.initChangDuanInfo(service.changDuanId);
            }
            return service.getChangDuanInfo();
        }

        public Integer getCurrentPosition() {
            if (service.changDuanInfo != null) {
                return 0;
            }
            return 0;
        }

        public void start() {
            Log.i(service.TAG, "start: ");
        }

        public void pause() {
            Log.i(service.TAG, "pause: ");
        }

        public void stop() {
            Log.i(service.TAG, "stop: ");
        }
    }
}
