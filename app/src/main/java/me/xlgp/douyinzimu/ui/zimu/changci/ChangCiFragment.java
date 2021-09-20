package me.xlgp.douyinzimu.ui.zimu.changci;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Objects;

import me.xlgp.douyinzimu.databinding.ChangCiFragmentBinding;
import me.xlgp.douyinzimu.model.ChangCi;
import me.xlgp.douyinzimu.obj.PingLun;
import me.xlgp.douyinzimu.obj.changduan.ChangCiList;
import me.xlgp.douyinzimu.service.PingLunService;
import me.xlgp.douyinzimu.ui.zimu.ZimuViewModel;

public class ChangCiFragment extends Fragment {

    private ChangCiViewModel mViewModel;
    private ChangCiFragmentBinding binding;

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

        mViewModel.changDuanState.observe(getViewLifecycleOwner(), s -> {
            if (s != null) Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
        });
        //观察唱词信息
        mViewModel.getChangDuanInfo().observe(getViewLifecycleOwner(), changDuanInfo -> {
            ChangCiList changCiList = changDuanInfo.getChangeCiList();
            changCiAdapter.updateData(changCiList);
            //todo 此处应该重新设计
            PingLunService.getInstance().setChangDuanInfo(changDuanInfo);
            updateRecyclerView(0);
            binding.pingLunSwitchMaterial.setChecked(changCiList.hasNext());
        });

        ZimuViewModel.getChangDuan().observe(getViewLifecycleOwner(), changDuan -> mViewModel.loadData(changDuan, (o, arg) -> {
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
        }));

        return binding.getRoot();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void updateTitleView(String text) {
        binding.currentZimuTitleTextView.setText(text);
    }

    private void updateRecyclerView(int position) {
        binding.zimuDetailRecyclerview.smoothScrollToPosition(position + 4);
    }

    private void pingLun(long delayMillis) {
        PingLunService.getInstance().start(delayMillis);
    }
}