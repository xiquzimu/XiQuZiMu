package me.xlgp.xiquzimu.worker;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.NoSuchElementException;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import me.xlgp.xiquzimu.data.ChangDuanRepository;
import me.xlgp.xiquzimu.data.FetchRemoteRepository;
import me.xlgp.xiquzimu.designpatterns.ObserverHelper;
import me.xlgp.xiquzimu.util.ChangDuanHelper;

public class InitChangDuanInfoWorker extends Worker {

    public InitChangDuanInfoWorker(@NonNull @NotNull Context context, @NonNull @NotNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @NotNull
    @Override
    public Result doWork() {
        if (isFirstLaunch()) {
            loadChangDuanInfo();
        }
        return Result.success();
    }

    private boolean isFirstLaunch() {
        String FIRST_LAUNCH = "first_launch";
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(FIRST_LAUNCH, Context.MODE_PRIVATE);
        String IS_FIRST = "is_first";
        boolean isFirst = sharedPreferences.getBoolean(IS_FIRST, true);
        if (isFirst) {
            sharedPreferences.edit().putBoolean(IS_FIRST, false).apply();
        }
        return isFirst;
    }

    private void loadChangDuanInfo() {
        ChangDuanRepository changDuanRepository = new ChangDuanRepository();
        FetchRemoteRepository fetchRemoteRepository = new FetchRemoteRepository();
        changDuanRepository.updateList()
                .flatMap((Function<List<String>, ObservableSource<String>>) list -> {
                    if (list.size() == 0) throw new NoSuchElementException("没有远程数据");
                    return Observable.fromIterable(list);
                }).flatMap((Function<String, ObservableSource<List<String>>>) s -> fetchRemoteRepository.changDuan(s.substring(1)))
                .flatMap((Function<List<String>, ObservableSource<Long>>) list -> changDuanRepository.saveAynsc(ChangDuanHelper.parse(list)))
                .compose(ObserverHelper.transformer()).subscribe(new ChangDuanInfoObserver());
    }

    static class ChangDuanInfoObserver implements Observer<Long> {
        private Disposable disposable;

        @Override
        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
            disposable = d;
        }

        @Override
        public void onNext(@io.reactivex.rxjava3.annotations.NonNull Long aLong) {

        }


        @Override
        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
            dispose();
        }

        @Override
        public void onComplete() {
            dispose();
        }

        private void dispose() {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }
}
