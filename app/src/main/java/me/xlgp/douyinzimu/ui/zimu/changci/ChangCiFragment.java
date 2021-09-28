package me.xlgp.douyinzimu.ui.zimu.changci;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
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

import io.reactivex.rxjava3.disposables.Disposable;
import me.xlgp.douyinzimu.constant.AppConstant;
import me.xlgp.douyinzimu.databinding.ChangCiFragmentBinding;
import me.xlgp.douyinzimu.model.ChangDuanInfo;
import me.xlgp.douyinzimu.service.PinglunLifecycleService;

public class ChangCiFragment extends Fragment {

    private ChangCiViewModel mViewModel;

    private ChangCiFragmentBinding binding;

    private ChangCiAdapter changCiAdapter = null;

    private PinglunLifecycleService.PinglunBinder pinglunBinder;

    private PinglunServiceConnection pinglunServiceConnection = null;

    private PinglunBroadcastReceiver pinglunBroadcastReceiver;

    public static ChangCiFragment newInstance() {
        return new ChangCiFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ChangCiFragmentBinding.inflate(inflater, container, false);
        initRecyclerview();
        mViewModel = new ViewModelProvider(this).get(ChangCiViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            changCiAdapter.setPosition(savedInstanceState.getInt("position", 0));
        }

        binding.pingLunSwitchMaterial.setChecked(false);
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
            if (pinglunBinder != null) {
                if (isChecked) pinglunBinder.start();
                else pinglunBinder.pause();
            }
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
        pinglunBroadcastReceiver = new PinglunBroadcastReceiver();
        requireContext().registerReceiver(pinglunBroadcastReceiver, pinglunBroadcastReceiver.getIntentFilter());
    }

    @Override
    public void onPause() {
        super.onPause();
        requireContext().unregisterReceiver(pinglunBroadcastReceiver);
        pinglunBroadcastReceiver = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pinglunServiceConnection != null) {
            requireContext().unbindService(pinglunServiceConnection);
            pinglunServiceConnection = null;
        }
    }

    class PinglunBroadcastReceiver extends BroadcastReceiver {

        public PinglunBroadcastReceiver() {
        }

        public IntentFilter getIntentFilter() {
            IntentFilter intentFilter = new IntentFilter();
            String intentFilterAction = AppConstant.INTENT_FILTER_ACTION;
            intentFilter.addAction(intentFilterAction);
            return intentFilter;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String content = intent.getStringExtra("content");
            int position = intent.getIntExtra("position", 0);
            boolean enable = intent.getBooleanExtra("enable", true);
            if (!enable) binding.pingLunSwitchMaterial.setChecked(false);
            updateRecyclerView(position);
            updateTitleView(content);
        }
    }

    class PinglunServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            pinglunBinder = (PinglunLifecycleService.PinglunBinder) service;
            pinglunBinder.load().subscribe(new ChangDuanInfoObservable());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

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
            binding.pingLunSwitchMaterial.setChecked(false);
            binding.pingLunSwitchMaterial.setChecked(true);
            finish();
        }

        @Override
        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
            mViewModel.changDuanState.postValue(e.getMessage());
            binding.pingLunSwitchMaterial.setChecked(false);
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