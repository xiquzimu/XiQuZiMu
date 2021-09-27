package me.xlgp.douyinzimu.service;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import me.xlgp.douyinzimu.designpatterns.ObserverHelper;
import me.xlgp.douyinzimu.model.ChangCi;
import me.xlgp.douyinzimu.obj.Callback;
import me.xlgp.douyinzimu.util.PingLunHelper;

public class PingLunService {

    //标记是否按当前唱词间隔时间
    public static Integer CURRENT_MILLIS = -1;

    //todo 此处应该重构
    private long count = 0; //记录线程数量，用于判断即将执行的线程是不是当前应当执行的线程

    public void start(long delayMillis) {
        count++;
        Observable.just(count).delay(delayMillis, TimeUnit.MILLISECONDS).compose(ObserverHelper.transformer())
                .subscribe(new StartObserver(this));
    }

    public void run(ChangCi changCi, Callback<Boolean> callback) {
        try {
            PingLunHelper.input(DouYinAccessibilityService.getInstance(), changCi, callback);
        } catch (Exception e) {
            Log.e("TAG", "run: ", e);
        }
    }

    private static class StartObserver implements Observer<Long> {
        private final PingLunService pingLunService;
        private Disposable disposable;

        public StartObserver(PingLunService pingLunService) {
            this.pingLunService = pingLunService;
        }

        @Override
        public void onSubscribe(@NonNull Disposable d) {
            disposable = d;
        }

        @Override
        public void onNext(@NonNull Long count) {
            try {
                if (count == pingLunService.count) {
                    PingLunHelper.openInputLayout(DouYinAccessibilityService.getInstance());
                }
            } catch (Exception e) {
                Log.e("TAG", "run: ", e);
                dispose();
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {
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
    }
}
