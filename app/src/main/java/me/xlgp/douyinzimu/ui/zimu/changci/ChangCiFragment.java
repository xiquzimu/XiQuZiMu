package me.xlgp.douyinzimu.ui.zimu.changci;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Observable;
import java.util.Observer;

import io.reactivex.rxjava3.disposables.Disposable;
import me.xlgp.douyinzimu.databinding.ChangCiFragmentBinding;
import me.xlgp.douyinzimu.model.ChangCi;
import me.xlgp.douyinzimu.model.ChangDuanInfo;
import me.xlgp.douyinzimu.service.PinglunLifecycleService;

public class ChangCiFragment extends Fragment {

    private ChangCiViewModel mViewModel;
    private ChangCiFragmentBinding binding;
    private ChangCiAdapter changCiAdapter = null;
    private PinglunLifecycleService.PinglunBinder pinglunBinder;
    private PinglunServiceConnection pinglunServiceConnection = null;

    public static ChangCiFragment newInstance() {
        return new ChangCiFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ChangCiFragmentBinding.inflate(inflater, container, false);
        initRecyclerview();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ChangCiViewModel.class);

        if (savedInstanceState != null) {
            changCiAdapter.setPosition(savedInstanceState.getInt("position", 0));
        }

        binding.pingLunSwitchMaterial.setOnCheckedChangeListener(getOnCheckedChangeListener());

        mViewModel.changDuanState.observe(getViewLifecycleOwner(), s -> {
            if (s != null) Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
        });
        //观察唱词信息
        mViewModel.changDuanInfo.observe(getViewLifecycleOwner(), changDuanInfo -> {
            changCiAdapter.updateData(changDuanInfo.getChangeCiList());
            updateRecyclerView(changCiAdapter.getPosition());
            updateTitleView(changDuanInfo.getChangeCiList().current().getContent());
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        startService();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (pinglunBinder != null) {
            outState.putInt("position", pinglunBinder.getCurrentPosition());
        }
    }

    private CompoundButton.OnCheckedChangeListener getOnCheckedChangeListener() {
        return (buttonView, isChecked) -> {
            if (isChecked) pinglunBinder.start();
            else pinglunBinder.pause();
        };
    }

    private void startService() {
        pinglunServiceConnection = new PinglunServiceConnection();
        requireContext().bindService(new Intent(requireContext(), PinglunLifecycleService.class), pinglunServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void initRecyclerview() {
        binding.zimuDetailRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));

        changCiAdapter = new ChangCiAdapter();
        changCiAdapter.setOnItemClickListener((itemView, view, data, position) -> {
            pinglunBinder.start(position);
            updateRecyclerView(pinglunBinder.getCurrentPosition());
            updateTitleView(data.getContent());
        });
        binding.zimuDetailRecyclerview.setAdapter(changCiAdapter);
    }

    private void updateTitleView(String text) {
        binding.currentZimuTitleTextView.setText(text);
    }

    private void updateRecyclerView(int position) {
        if (position < 4) binding.zimuDetailRecyclerview.smoothScrollToPosition(position);
        else binding.zimuDetailRecyclerview.smoothScrollToPosition(position + 4);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (pinglunBinder != null) {
            pinglunBinder.observe(new PinglunObserver(this));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (pinglunBinder != null) {
            pinglunBinder.observe(null);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pinglunServiceConnection != null)
            requireContext().unbindService(pinglunServiceConnection);
        Log.i("TAG", "onDestroy: ");
    }

    static class PinglunObserver implements Observer {
        private ChangCiFragment fragment;

        public PinglunObserver(ChangCiFragment changCiFragment) {
            fragment = changCiFragment;
        }

        @Override
        public void update(Observable o, Object arg) {
            ChangCi changCi = (ChangCi) arg;
            fragment.updateTitleView(changCi.getContent());
        }
    }

    class PinglunServiceConnection implements ServiceConnection {
        public PinglunServiceConnection() {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            pinglunBinder = (PinglunLifecycleService.PinglunBinder) service;
            pinglunBinder.load().subscribe(new ChangDuanInfoObservable());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onBindingDied(ComponentName name) {

        }

        @Override
        public void onNullBinding(ComponentName name) {

        }
    }

    class ChangDuanInfoObservable implements io.reactivex.rxjava3.core.Observer<ChangDuanInfo> {
        Disposable disposable;

        @Override
        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
            disposable = d;
        }

        @Override
        public void onNext(@io.reactivex.rxjava3.annotations.NonNull ChangDuanInfo changDuanInfo) {
            mViewModel.changDuanInfo.postValue(changDuanInfo);
            finish();
        }

        @Override
        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
            mViewModel.changDuanState.postValue(e.getMessage());
            finish();
        }

        @Override
        public void onComplete() {
            finish();
        }

        private void finish() {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }
}