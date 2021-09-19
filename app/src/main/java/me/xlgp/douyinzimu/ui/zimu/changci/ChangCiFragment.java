package me.xlgp.douyinzimu.ui.zimu.changci;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Objects;
import java.util.Observable;

import me.xlgp.douyinzimu.databinding.ChangCiFragmentBinding;
import me.xlgp.douyinzimu.listener.OnDouYinLiveListener;
import me.xlgp.douyinzimu.model.ChangCi;
import me.xlgp.douyinzimu.obj.PingLun;
import me.xlgp.douyinzimu.obj.changduan.ChangCiList;
import me.xlgp.douyinzimu.obj.changduan.ChangDuanInfo;
import me.xlgp.douyinzimu.service.DouYinAccessibilityService;
import me.xlgp.douyinzimu.service.PingLunService;
import me.xlgp.douyinzimu.ui.zimu.ZimuViewModel;

public class ChangCiFragment extends Fragment implements OnDouYinLiveListener {

    private ChangCiViewModel mViewModel;
    private ChangCiFragmentBinding binding;
    private boolean liveable =false;

    public static ChangCiFragment newInstance() {
        return new ChangCiFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ChangCiFragmentBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(ChangCiViewModel.class);

        binding.zimuDetailRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));

        ChangCiAdapter changCiAdapter = new ChangCiAdapter();
        changCiAdapter.setOnItemClickListener((itemView, view, data, position) -> {
            PingLunService.getInstance().getChangDuanInfo().getChangeCiList(position);
            pingLun(0);
            updateTitleView(data.getContent());
        });
        binding.zimuDetailRecyclerview.setAdapter(changCiAdapter);

        binding.pingLunSwitchMaterial.setOnCheckedChangeListener((buttonView, isChecked) -> {
            PingLun.getInstance().change(isChecked);
            if (PingLun.getInstance().disabled()) {
                Toast.makeText(requireContext(), "评论已关闭", Toast.LENGTH_SHORT).show();
                return;
            } else if (!PingLunService.getInstance().hasChangeCi()) {
                Toast.makeText(requireContext(), "没有选择唱段", Toast.LENGTH_SHORT).show();
                return;
            }
            pingLun(PingLunService.getInstance().getChangDuanInfo().getChangeCiList().current().getDelayMillis());
            Toast.makeText(requireContext(), "开始评论", Toast.LENGTH_SHORT).show();
        });

        mViewModel.loading.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.zimuDetailSwipeRefreshLayout.setRefreshing(aBoolean);
            }
        });
        mViewModel.changDuanState.observe(getViewLifecycleOwner(), s -> {
            if (s != null) Toast.makeText(requireContext(),s,Toast.LENGTH_SHORT).show();
        });
        mViewModel.getChangDuanInfo().observe(getViewLifecycleOwner(), new Observer<ChangDuanInfo>() {
            @Override
            public void onChanged(ChangDuanInfo changDuanInfo) {
                ChangCiList changCiList = changDuanInfo.getChangeCiList();
                changCiAdapter.updateData(changCiList);
                //todo 此处应该重新设计
                PingLunService.getInstance().setChangDuanInfo(changDuanInfo);
                updateRecyclerView(0);
                binding.pingLunSwitchMaterial.setChecked(liveable && changCiList.hasNext());
            }
        });
        ZimuViewModel.getChangDuan().observe(getViewLifecycleOwner(), changDuan -> mViewModel.loadData(Objects.requireNonNull(ZimuViewModel.getChangDuan().getValue()), (o, arg) -> {
            ChangCi changCi = (ChangCi) arg;
            updateTitleView(changCi.getContent());
            ChangCiList changCiList = Objects.requireNonNull(mViewModel.getChangDuanInfo().getValue()).getChangeCiList();
            updateRecyclerView(changCiList.currentIndex());
            if (!changCiList.hasNext()) {
                binding.pingLunSwitchMaterial.setChecked(false);
            }
        }));

        return binding.getRoot();

    }

    @Override
    public void onStart() {
        super.onStart();
        addDouYinObserver();
    }

    private void updateTitleView(String text) {
        binding.currentZimuTitleTextView.setText(text);
    }
    private void updateRecyclerView(int position) {
        binding.zimuDetailRecyclerview.smoothScrollToPosition(position + 4);
    }

    private void pingLun(long delayMillis) {
        if (liveable) {
            PingLunService.getInstance().start(delayMillis);
        }
    }

    @Override
    public void onLive(boolean isLive) {
        liveable = isLive;
        if (!liveable) {
            binding.pingLunSwitchMaterial.setChecked(false);
        }
    }

    private void addDouYinObserver() {
        DouYinAccessibilityService douYinAccessibilityService = DouYinAccessibilityService.getInstance();
        if (douYinAccessibilityService != null) {
            douYinAccessibilityService.addObserver(new DouYinObserver());
        }
    }

    class DouYinObserver implements java.util.Observer {
        private boolean liveable;

        public DouYinObserver() {
            liveable = false;
        }

        @Override
        public void update(Observable o, Object arg) {
            boolean liveable = (boolean) arg;
            if (liveable != this.liveable) {
                this.liveable = liveable;
                onLive(liveable);
            }
        }
    }
}