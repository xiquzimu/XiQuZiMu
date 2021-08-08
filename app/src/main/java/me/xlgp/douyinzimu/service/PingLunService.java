package me.xlgp.douyinzimu.service;

import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import me.xlgp.douyinzimu.exception.NotFoundDouYinException;
import me.xlgp.douyinzimu.obj.PingLun;
import me.xlgp.douyinzimu.obj.changduan.ChangCiList;
import me.xlgp.douyinzimu.obj.changduan.ChangDuan;
import me.xlgp.douyinzimu.util.PingLunHelper;

public class PingLunService {

    private static PingLunService instance = null;
    private ChangDuan changDuan = null;
    private DouYinAccessibilityService douYinAccessibilityService;

    public static PingLunService getInstance(DouYinAccessibilityService douYinAccessibilityService) {
        if (instance == null) {
            instance = new PingLunService();
        }
        if (douYinAccessibilityService != null) {
            instance.douYinAccessibilityService = douYinAccessibilityService;
        }
        return instance;
    }

    //todo 此处应该重构
    private long count = 0; //记录线程数量，用于判断即将执行的线程是不是当前应当执行的线程

    public void start(long delayMillis) {
        count++;
        if (enablePingLun()) {
            Observable.just(count).delay(delayMillis, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new StartObserver(instance));
        }
    }

    public ChangDuan getChangDuan() {
        return changDuan;
    }

    public void setChangDuan(ChangDuan changDuan) {
        this.changDuan = changDuan;
    }

    public void clear() {
        changDuan = null;
    }

    public boolean hasChangeCi() {
        return changDuan != null && changDuan.getChangeCiList().hasNext();
    }

    public boolean enablePingLun() {
        return !PingLun.getInstance().disabled() && hasChangeCi();
    }

    public void run() {
        if (enablePingLun()) {
            ChangCiList changCiList = changDuan.getChangeCiList();
            PingLunHelper.input(douYinAccessibilityService, changCiList.next(), this::start);
        }
    }

    private class StartObserver implements Observer<Long> {
        private PingLunService pingLunService;

        public StartObserver(PingLunService pingLunService) {
            this.pingLunService = pingLunService;
        }

        @Override
        public void onSubscribe(@NonNull Disposable d) {
        }

        @Override
        public void onNext(@NonNull Long count) {
            try {
                if (count == pingLunService.count) {
                    PingLunHelper.openInputLayout(douYinAccessibilityService);
                }
            } catch (Exception e) {
                if (e instanceof NotFoundDouYinException) {
                    Toast.makeText(douYinAccessibilityService, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {
            if (e instanceof NotFoundDouYinException) {
                Toast.makeText(douYinAccessibilityService, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onComplete() {
        }
    }
}
