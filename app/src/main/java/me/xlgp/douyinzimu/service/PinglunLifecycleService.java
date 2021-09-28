package me.xlgp.douyinzimu.service;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleService;

import java.util.NoSuchElementException;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import me.xlgp.douyinzimu.constant.AppConstant;
import me.xlgp.douyinzimu.data.ChangDuanInfoRepository;
import me.xlgp.douyinzimu.model.ChangCi;
import me.xlgp.douyinzimu.model.ChangCiList;
import me.xlgp.douyinzimu.model.ChangDuanInfo;

public class PinglunLifecycleService extends LifecycleService {

    private final String TAG = PinglunLifecycleService.class.getName();

    public static final String CHANG_DUAN_ID = "changDuanId";

    private int changDuanId = 0;

    private ChangDuanInfo changDuanInfo;
    private ChangCiList changCiList;

    //标志能否评论
    private boolean call = false;

    private PingLunService pingLunService;

    private PinglunBinder pinglunBinder;

    @Override
    public void onCreate() {
        super.onCreate();
        setPinglunRunListener();
    }

    private void setPinglunRunListener() {
        DouYinAccessibilityService accessibilityService = DouYinAccessibilityService.getInstance();
        if (accessibilityService != null) {
            accessibilityService.onPinglunRunListener(this::run);
        }
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
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
        pinglunBinder = new PinglunBinder(this);
        return pinglunBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        call = false;
        return super.onUnbind(intent);
    }

    private void init() {
        if (this.changDuanInfo == null) {
            pingLunService = new PingLunService();
            call = false;
            return;
        }
        if (!this.changDuanInfo.getChangDuan().getId().equals(changDuanId)) {
            this.changDuanInfo = null;
            pingLunService = new PingLunService();
            call = false;
        }
    }

    private Observable<ChangDuanInfo> getChangDuanInfo() {
        return Observable.create(emitter -> {
            if (changDuanInfo == null) {
                emitter.onError(new NoSuchElementException("没有唱词"));
            } else {
                emitter.onNext(changDuanInfo);
            }
        });
    }

    private Observable<ChangDuanInfo> initChangDuanInfo(Integer changDuanId) {
        ChangDuanInfoRepository changDuanInfoRepository = new ChangDuanInfoRepository();
        return changDuanInfoRepository.getChangDuanInfo(changDuanId).flatMap((Function<ChangDuanInfo,
                ObservableSource<ChangDuanInfo>>) changDuanInfo -> {
            this.changDuanInfo = changDuanInfo;
            this.changCiList = changDuanInfo.getChangeCiList(0);
            return getChangDuanInfo();
        });

    }

    private boolean enablePinglun() {
        return changDuanInfo != null && changCiList != null && changCiList.hasNext();
    }

    private void pinglun(long currentMillis) {
        if (call && enablePinglun()) {
            pingLunService.start(currentMillis);
        }
    }

    private void pinglun() {
        if (call && enablePinglun()) {
            pinglun(changCiList.current().getDelayMillis());
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        call = false;
        if (pingLunService != null) {
            pingLunService.disable();
            pingLunService = null;
        }
    }

    public void run() {
        if (call && enablePinglun()) {
            ChangCi changCi = changCiList.next();
            sendPinglunBroadcast(changCi);
            pingLunService.run(changCi.getContent(), aBoolean -> {
                if (enablePinglun()) {
                    pinglun();
                } else sendPinglunBroadcast(false);
            });
        }
    }

    private void sendPinglunBroadcast(boolean b) {
        Intent intent = new Intent();
        intent.setAction(AppConstant.INTENT_FILTER_ACTION);
        intent.putExtra("enable", b);
        sendBroadcast(intent);
    }

    private void sendPinglunBroadcast(ChangCi changCi) {
        Intent intent = new Intent();
        intent.setAction(AppConstant.INTENT_FILTER_ACTION);
        intent.putExtra("content", changCi.getContent());
        intent.putExtra("position", pinglunBinder.getCurrentPosition());
        sendBroadcast(intent);
    }

    public static class PinglunBinder extends Binder {
        PinglunLifecycleService service;

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
            if (service.enablePinglun()) return service.changCiList.currentIndex();
            return 0;
        }

        public void start(int position) {
            if (service.changCiList != null && !service.changCiList.isEmpty()) {
                service.changDuanInfo.getChangeCiList(position);
            }
            service.pinglun(PingLunService.CURRENT_MILLIS);
        }

        public void start() {
            service.call = true;
            service.pinglun();
        }

        public void pause() {
            service.call = false;
        }
    }
}
