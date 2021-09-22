package me.xlgp.douyinzimu.ui.zimu.changci;

import android.os.Bundle;
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

import java.util.Objects;
import java.util.Observer;

import me.xlgp.douyinzimu.databinding.ChangCiFragmentBinding;
import me.xlgp.douyinzimu.model.ChangCi;
import me.xlgp.douyinzimu.obj.PingLun;
import me.xlgp.douyinzimu.obj.changduan.ChangCiList;
import me.xlgp.douyinzimu.obj.changduan.ChangDuanInfo;
import me.xlgp.douyinzimu.service.PingLunService;
import me.xlgp.douyinzimu.ui.zimu.ZimuViewModel;

public class ChangCiFragment extends Fragment {

    private ChangCiViewModel mViewModel;
    private ChangCiFragmentBinding binding;
    private ChangCiAdapter changCiAdapter = null;

    public static ChangCiFragment newInstance() {
        return new ChangCiFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ChangCiFragmentBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(ChangCiViewModel.class);

        initRecyclerview();

        binding.pingLunSwitchMaterial.setOnCheckedChangeListener(getOnCheckedChangeListener());

        mViewModel.changDuanState.observe(getViewLifecycleOwner(), s -> {
            if (s != null) Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
        });
        //观察唱词信息
        mViewModel.getChangDuanInfo().observe(getViewLifecycleOwner(), getChangDuanInfoObserver());

        ZimuViewModel.getChangDuan().observe(getViewLifecycleOwner(), changDuan -> mViewModel.loadData(changDuan, getChangDuanObserver()));
        return binding.getRoot();

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
            ChangCiList changCiList = changDuanInfo.getChangeCiList();
            changCiAdapter.updateData(changCiList);
            //todo 此处应该重新设计
            PingLunService.getInstance().setChangDuanInfo(changDuanInfo);
            updateRecyclerView(0);
            binding.pingLunSwitchMaterial.setChecked(false);
            binding.pingLunSwitchMaterial.setChecked(changCiList.hasNext());
        };
    }

    private Observer getChangDuanObserver() {
        return (o, arg) -> {
            try {
                ChangCi changCi = (ChangCi) arg;
                updateTitleView(changCi.getContent());
                ChangCiList changCiList = Objects.requireNonNull(mViewModel.getChangDuanInfo().getValue()).getChangeCiList();
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
}