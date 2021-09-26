package me.xlgp.douyinzimu.service;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleService;

import me.xlgp.douyinzimu.data.ChangCiRepository;

public class PinglunLifecycleService extends LifecycleService {

    private final String TAG = PinglunLifecycleService.class.getName();

    private Integer changDuanId;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: " + toString());
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: " + flags + " " + startId + " " + toString());
        if (intent != null) {
            changDuanId = intent.getIntExtra("changDuanId", -1);
            if (changDuanId > 0) initChangDuanInfo();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void initChangDuanInfo(){
        ChangCiRepository changCiRepository = new ChangCiRepository();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
