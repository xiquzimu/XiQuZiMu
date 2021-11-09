package me.xlgp.xiquzimu.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import me.xlgp.xiquzimu.util.APKHelper;

public class ClearApklWorker extends Worker {
    public ClearApklWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            APKHelper.removeApk(getApplicationContext());
            return Result.success();
        } catch (Exception e) {
            return Result.failure();
        }
    }
}
