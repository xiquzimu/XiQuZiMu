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
import me.xlgp.douyinzimu.model.ChangCiList;
import me.xlgp.douyinzimu.model.ChangDuanInfo;

public class PinglunLifecycleService extends LifecycleService implements ViewModelStoreOwner, OnPinglunRunListener {

    private final String TAG = PinglunLifecycleService.class.getName();

    public static final String CHANG_DUAN_ID = "changDuanId";

    private int changDuanId = 0;

    private ChangDuanInfo changDuanInfo;
    private ChangCiList changCiList;

    //标志能否评论
    private boolean call = false;

    private PinglunViewModel viewModel;

    private DouYinAccessibilityService accessibilityService;

    private PingLunService pingLunService;

    public PinglunLifecycleService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: " + toString());
        accessibilityService = DouYinAccessibilityService.getInstance();
        accessibilityService.onPinglunRunListener(this);
        viewModel = new ViewModelProvider(this).get(PinglunViewModel.class);
        viewModel.getChangDuanInfo().observe(this, changDuanInfo -> {
            this.changDuanInfo = changDuanInfo;
            this.changCiList = changDuanInfo.getChangeCiList(0);
            call = true;
            //开始pinglun
            pinglun();
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
            pingLunService = new PingLunService();
            call = false;
        }
    }

    private Observable<ChangDuanInfo> getChangDuanInfo() {
        return Observable.create(emitter -> {
            if (changDuanInfo == null)
                emitter.onError(new NoSuchElementException("没有唱词"));
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

    private void pinglun(long currentMillis) {
        if (call) {
            pingLunService.start(currentMillis);
        }
    }

    private void pinglun() {
        if (call) {
            pingLunService.start(changCiList.current().getDelayMillis());
        }
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

    @Override
    public void onRun() {
        if (call) {
            pingLunService.run(changCiList.next(), aBoolean -> pinglun(PingLunService.CURRENT_MILLIS));
        }
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
        private int position = 0;

        public PinglunBinder(PinglunLifecycleService service) {
            this.service = service;
        }


        public Observable<ChangDuanInfo> load() {
            if (service.changDuanInfo == null && service.changDuanId > 0) {
                return service.initChangDuanInfo(service.changDuanId);
            }
            return service.getChangDuanInfo();
        }

        public Integer getCurrentPosition() {
            return position;
        }

        public void start(int position) {
            if (service.call) {
                Log.i(service.TAG, "start: " + position);
                service.changDuanInfo.getChangeCiList(position);
                service.call = service.changCiList.hasNext();
                service.pinglun(PingLunService.CURRENT_MILLIS);
                this.position = position;
            }
        }

        public void start() {
            service.pinglun();
        }

        public void pause() {
            service.call = false;
        }

        public void stop() {
            Log.i(service.TAG, "stop: ");
            service.call = false;
        }
    }
}
