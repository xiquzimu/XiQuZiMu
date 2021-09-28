package me.xlgp.douyinzimu.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentController;
import androidx.fragment.app.FragmentHostCallback;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.obj.ZWindowManager;
import me.xlgp.douyinzimu.obj.ZimuLayoutParams;
import me.xlgp.douyinzimu.ui.floating.toolbar.FloatingToolBarFragment;
import me.xlgp.douyinzimu.ui.zimu.main.ZimuMainFragment;

public class FloatingService extends LifecycleService {

    final FragmentController mFragments = FragmentController.createController(new FloatingHostCallbacks());
    private View rootView;

    @Override
    public void onCreate() {
        super.onCreate();
        mFragments.attachHost(null);
    }

    @Override
    public int onStartCommand(@Nullable @org.jetbrains.annotations.Nullable Intent intent, int flags, int startId) {
        if (rootView == null) {
            addView();
            mFragments.dispatchResume();
            addFragment();
        } else {
            Toast.makeText(this, "已开启悬浮窗口", Toast.LENGTH_SHORT).show();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @SuppressLint("InflateParams")
    private void addView() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = inflater.inflate(R.layout.view_floating_container, null);
        ZWindowManager.getInstance().addView(rootView, new ZimuLayoutParams.WithFullWidth());
        rootView.setVisibility(View.GONE);
    }

    public View getRootView() {
        return rootView;
    }

    void addFragment() {

        mFragments.getSupportFragmentManager().beginTransaction()
                .add(R.id.floatingToolBar, FloatingToolBarFragment.Factory.create())
                .add(R.id.floatingContainer, ZimuMainFragment.newInstance()).runOnCommit(() -> rootView.setVisibility(View.VISIBLE)).commit();
    }


    public void stop() {
        stopSelf();
    }

    @Override
    public void onDestroy() {
        mFragments.dispatchDestroy();
        rootView.setVisibility(View.GONE);
        ZWindowManager.getInstance().removeView(rootView);
        super.onDestroy();
    }

    class FloatingHostCallbacks extends FragmentHostCallback<FloatingService> implements ViewModelStoreOwner {

        public FloatingHostCallbacks() {
            super(FloatingService.this, new Handler(Looper.getMainLooper()), 0);
        }

        @Nullable
        @Override
        public FloatingService onGetHost() {
            return FloatingService.this;
        }

        @Nullable
        @Override
        public View onFindViewById(int id) {
            return rootView.findViewById(id);
        }

        @NonNull
        @Override
        public LayoutInflater onGetLayoutInflater() {
            return LayoutInflater.from(FloatingService.this).cloneInContext(FloatingService.this);
        }

        @NonNull
        @Override
        public ViewModelStore getViewModelStore() {
            return new ViewModelStore();
        }
    }
}
