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

import me.xlgp.douyinzimu.databinding.ChangCiFragmentBinding;
import me.xlgp.douyinzimu.model.ChangCi;
import me.xlgp.douyinzimu.obj.changduan.ChangCiList;
import me.xlgp.douyinzimu.obj.changduan.ChangDuanInfo;
import me.xlgp.douyinzimu.ui.zimu.ZimuViewModel;
import me.xlgp.douyinzimu.view.ChangCiAdapter;

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
            //todo 点击唱词 发送
            updateTitleView(data.getContent());
        });
        binding.zimuDetailRecyclerview.setAdapter(changCiAdapter);

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
                changCiAdapter.updateData(changDuanInfo.getChangeCiList());
                updateRecyclerView(0);
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

    private void updateTitleView(String text) {
        binding.currentZimuTitleTextView.setText(text);
    }
    private void updateRecyclerView(int position) {
        binding.zimuDetailRecyclerview.smoothScrollToPosition(position + 4);
    }
}