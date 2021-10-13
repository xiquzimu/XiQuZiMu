package me.xlgp.xiquzimu.ui.zimu.changci;

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
import me.xlgp.xiquzimu.constant.AppConstant;
import me.xlgp.xiquzimu.databinding.ChangCiFragmentBinding;
import me.xlgp.xiquzimu.model.ChangCiList;
import me.xlgp.xiquzimu.service.PinglunLifecycleService;

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

        binding.pingLunSwitchMaterial.setOnCheckedChangeListener(getOnCheckedChangeListener());

        mViewModel.changDuanState.observe(getViewLifecycleOwner(), s -> {
            if (s != null) Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
        });
        //观察唱词信息
        mViewModel.changCiList.observe(getViewLifecycleOwner(), changCiList -> {
            changCiAdapter.updateData(changCiList);
            binding.pingLunSwitchMaterial.setChecked(false);
            if (changCiList.hasNext()) {
                updateRecyclerView(changCiList.currentIndex());
                updateTitleView(changCiList.current().getContent());
                binding.pingLunSwitchMaterial.setChecked(true);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        startService();
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
            updateRecyclerView(position);
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
            pinglunBinder = null;
        }
    }

    class PinglunBroadcastReceiver extends BroadcastReceiver {

        public IntentFilter getIntentFilter() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(AppConstant.INTENT_FILTER_ACTION);
            return intentFilter;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra("action");

            if (AppConstant.BROADCAST_ACTION_DONE.equals(action)) {
                //发送完毕
                boolean enable = intent.getBooleanExtra("enable", true);
                if (!enable) binding.pingLunSwitchMaterial.setChecked(false);

            } else if (AppConstant.BROADCAST_ACTION_START.equals(action)) {
                //开发发送
                String content = intent.getStringExtra("content");
                int position = intent.getIntExtra("position", -1);
                updateRecyclerView(position);
                changCiAdapter.hightLightItem(position);
                updateTitleView(content);
            }
        }
    }

    class PinglunServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            pinglunBinder = (PinglunLifecycleService.PinglunBinder) service;
            pinglunBinder.load().subscribe(new ChangCiListObservable());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    class ChangCiListObservable implements io.reactivex.rxjava3.core.Observer<ChangCiList> {
        Disposable disposable;

        @Override
        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
            disposable = d;
        }

        @Override
        public void onNext(@io.reactivex.rxjava3.annotations.NonNull ChangCiList changCiList) {
            mViewModel.changCiList.postValue(changCiList);
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