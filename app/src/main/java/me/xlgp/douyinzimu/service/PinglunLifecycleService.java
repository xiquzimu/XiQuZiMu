package me.xlgp.douyinzimu.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import me.xlgp.douyinzimu.constant.DouYinConstant;
import me.xlgp.douyinzimu.data.ChangDuanInfoRepository;
import me.xlgp.douyinzimu.model.ChangCi;
import me.xlgp.douyinzimu.model.ChangCiList;
import me.xlgp.douyinzimu.model.ChangDuanInfo;

public class PinglunLifecycleService extends LifecycleService {

    private final String TAG = PinglunLifecycleService.class.getName();

    public static final String CHANG_DUAN_ID = "changDuanId";

    private int changDuanId = -1;

    private ChangCiList changCiList;

    //标志能否评论
    private boolean call = false;

    private PingLunService pingLunService;

    private PinglunBinder pinglunBinder;

    private DouYinBroadcastReceiver douYinBroadcastReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        douYinBroadcastReceiver = new DouYinBroadcastReceiver();
        registerReceiver(douYinBroadcastReceiver, douYinBroadcastReceiver.getIntentFilter());
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        if (intent != null) {
            int changDuanId = intent.getIntExtra(CHANG_DUAN_ID, -1);
            if (changDuanId > 0) {
                init(changDuanId);
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

    private void init(int changDuanId) {
        if (this.changDuanId != changDuanId) {
            changCiList = null;
            pingLunService = new PingLunService();
            call = false;
            this.changDuanId = changDuanId;
        } else if (changCiList == null) {
            pingLunService = new PingLunService();
            call = false;
        }
    }

    private Observable<ChangCiList> getChangCiList() {
        return Observable.create(emitter -> {
            if (changCiList == null || changCiList.isEmpty()) {
                emitter.onError(new NoSuchElementException("没有唱词"));
            } else {
                emitter.onNext(changCiList);
            }
        });
    }

    private Observable<ChangCiList> initChangDuanInfo(Integer changDuanId) {
        ChangDuanInfoRepository changDuanInfoRepository = new ChangDuanInfoRepository();
        return changDuanInfoRepository.getChangDuanInfo(changDuanId).flatMap((Function<ChangDuanInfo,
                ObservableSource<ChangCiList>>) changDuanInfo -> {
            this.changCiList = changDuanInfo.getChangeCiList(0);
            return getChangCiList();
        });
    }

    private boolean enablePinglun() {
        return changCiList != null && changCiList.hasNext();
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
        changCiList = null;
        if (douYinBroadcastReceiver != null) {
            unregisterReceiver(douYinBroadcastReceiver);
        }
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
                } else sendPinglunBroadcast();
            });
        }
    }

    private void sendPinglunBroadcast() {
        Intent intent = new Intent();
        intent.setAction(AppConstant.INTENT_FILTER_ACTION);
        intent.putExtra("enable", false);
        sendBroadcast(intent);
    }

    private void sendPinglunBroadcast(ChangCi changCi) {
        Intent intent = new Intent();
        intent.setAction(AppConstant.INTENT_FILTER_ACTION);
        intent.putExtra("content", changCi.getContent());
        intent.putExtra("position", pinglunBinder.getCurrentPosition());
        sendBroadcast(intent);
    }

    class DouYinBroadcastReceiver extends BroadcastReceiver {
        public IntentFilter getIntentFilter() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(DouYinConstant.INTENT_DY_SERVICE_ACTION);
            return intentFilter;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DouYinConstant.INTENT_DY_SERVICE_ACTION)) {
                String action = intent.getStringExtra("action");
                if ("run".equals(action)) {
                    run();
                }
            }
        }
    }

    public static class PinglunBinder extends Binder {
        PinglunLifecycleService service;

        public PinglunBinder(PinglunLifecycleService service) {
            this.service = service;
        }

        public Observable<ChangCiList> load() {
            if (service.changCiList == null && service.changDuanId > 0) {
                return service.initChangDuanInfo(service.changDuanId);
            }
            return service.getChangCiList();
        }

        public Integer getCurrentPosition() {
            if (service.enablePinglun()) return service.changCiList.currentIndex();
            return 0;
        }

        public void start(int position) {
            if (service.changCiList != null && !service.changCiList.isEmpty()) {
                service.changCiList.setCursor(position);
            }
            service.pinglun(PingLunService.CURRENT_MILLIS);
        }

        public void start() {
            if (!service.call) {
                service.call = true;
                service.pinglun();
            }
        }

        public void pause() {
            service.call = false;
        }
    }
}
