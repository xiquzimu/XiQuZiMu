package me.xlgp.xiquzimu.service;

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

import me.xlgp.xiquzimu.R;
import me.xlgp.xiquzimu.obj.ZWindowManager;
import me.xlgp.xiquzimu.obj.ZimuLayoutParams;
import me.xlgp.xiquzimu.ui.floating.toolbar.FloatingToolBarFragment;
import me.xlgp.xiquzimu.ui.zimu.changci.ChangCiFragment;
import me.xlgp.xiquzimu.ui.zimu.main.ZimuMainFragment;

public class FloatingService extends LifecycleService {

    final FragmentController mFragments = FragmentController.createController(new FloatingHostCallbacks());
    private View rootView;

    private ChangCiFragment changCiFragment = null;
    private ZimuMainFragment zimuMainFragment = null;

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

    private void changeFragment(boolean changciEnable) {
        if (changciEnable) {
            mFragments.getSupportFragmentManager().beginTransaction().detach(zimuMainFragment).attach(changCiFragment).commit();
        } else {
            mFragments.getSupportFragmentManager().beginTransaction().detach(changCiFragment).attach(zimuMainFragment).commit();
        }
    }

    void addFragment() {

        changCiFragment = ChangCiFragment.newInstance();
        zimuMainFragment = ZimuMainFragment.newInstance();
        FloatingToolBarFragment floatingToolBarFragment = FloatingToolBarFragment.newInstance();

        floatingToolBarFragment.setOnFragmentChangeListener(this::changeFragment);

        zimuMainFragment.setOnFragmentChangeListener(change -> changeFragment(true));

        mFragments.getSupportFragmentManager().beginTransaction()
                .add(R.id.floatingToolBar, floatingToolBarFragment)
                .add(R.id.floatingContainer, zimuMainFragment)
                .add(R.id.floatingChangciContainer, changCiFragment).detach(changCiFragment)
                .runOnCommit(() -> rootView.setVisibility(View.VISIBLE)).commit();
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
