package me.xlgp.douyinzimu.ui.zimu.changci;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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

import java.io.Serializable;
import java.util.Objects;
import java.util.Observer;

import io.reactivex.rxjava3.disposables.Disposable;
import me.xlgp.douyinzimu.databinding.ChangCiFragmentBinding;
import me.xlgp.douyinzimu.model.ChangCi;
import me.xlgp.douyinzimu.model.ChangCiList;
import me.xlgp.douyinzimu.model.ChangDuan;
import me.xlgp.douyinzimu.model.ChangDuanInfo;
import me.xlgp.douyinzimu.obj.PingLun;
import me.xlgp.douyinzimu.service.PingLunService;
import me.xlgp.douyinzimu.service.PinglunLifecycleService;
import me.xlgp.douyinzimu.ui.zimu.main.ZimuMainFragment;

public class ChangCiFragment extends Fragment implements Serializable {

    private ChangCiViewModel mViewModel;
    private ChangCiFragmentBinding binding;
    private ChangCiAdapter changCiAdapter = null;
    private PinglunLifecycleService.PinglunBinder pinglunBinder;

    public static ChangCiFragment newInstance() {
        return new ChangCiFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ChangCiFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ChangCiViewModel.class);

        initRecyclerview();

        binding.pingLunSwitchMaterial.setOnCheckedChangeListener(getOnCheckedChangeListener());

        mViewModel.changDuanState.observe(getViewLifecycleOwner(), s -> {
            if (s != null) Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
        });
        //观察唱词信息
        mViewModel.changDuanInfo.observe(getViewLifecycleOwner(), changDuanInfo -> changCiAdapter.updateData(changDuanInfo.getChangeCiList()));

    }

    @Override
    public void onStart() {
        super.onStart();
        startService();
    }

    private CompoundButton.OnCheckedChangeListener getOnCheckedChangeListener() {
        return (buttonView, isChecked) -> {
            PingLun.getInstance().change(isChecked);
            if (PingLun.getInstance().disabled()) {
                Toast.makeText(requireContext(), "评论已关闭", Toast.LENGTH_SHORT).show();
                return;
            } else if (!PingLunService.getInstance().hasChangeCi()) {
                Toast.makeText(requireContext(), "没有选择唱段", Toast.LENGTH_SHORT).show();
                return;
            }
            pingLun(PingLunService.CURRENT_MILLIS);
            Toast.makeText(requireContext(), "开始评论", Toast.LENGTH_SHORT).show();
        };
    }

    private androidx.lifecycle.Observer<? super ChangDuanInfo> getChangDuanInfoObserver() {
        return changDuanInfo -> {
            if (changDuanInfo == null) return;

            ChangCiList changCiList = changDuanInfo.getChangeCiList();
            changCiAdapter.updateData(changCiList);
            //todo 此处应该重新设计
            PingLunService.getInstance().setChangDuanInfo(changDuanInfo);
            updateRecyclerView(0);
            binding.pingLunSwitchMaterial.setChecked(false);
            binding.pingLunSwitchMaterial.setChecked(changCiList.hasNext());
        };
    }

    private void startService() {
        Intent intent = new Intent(requireContext(), PinglunLifecycleService.class);
        assert getParentFragment() != null;
        ChangDuan changDuan = ((ZimuMainFragment) getParentFragment()).getChangDuan();
        if (changDuan == null) {
            return;
        }
        intent.putExtra("changDuanId", changDuan.getId());
        requireContext().bindService(intent, new PinglunServiceConnection(), Context.BIND_AUTO_CREATE);
    }

    private Observer getChangDuanObserver() {
        return (o, arg) -> {
            try {
                ChangCi changCi = (ChangCi) arg;
                updateTitleView(changCi.getContent());
                ChangCiList changCiList = Objects.requireNonNull(mViewModel.changDuanInfo.getValue()).getChangeCiList();
                updateRecyclerView(changCiList.currentIndex());
                if (!changCiList.hasNext()) {
                    binding.pingLunSwitchMaterial.setChecked(false);
                }
            } catch (Exception e) {
                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void initRecyclerview() {
        binding.zimuDetailRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));

        changCiAdapter = new ChangCiAdapter();
        changCiAdapter.setOnItemClickListener((itemView, view, data, position) -> {
            PingLunService.getInstance().setCurrentItem(position);
            pingLun(0);
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

    private void pingLun(long delayMillis) {
        PingLunService.getInstance().start(delayMillis);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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