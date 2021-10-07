package me.xlgp.xiquzimu.ui.zimu.changduan;

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

import me.xlgp.xiquzimu.adapter.BaseAdapter;
import me.xlgp.xiquzimu.databinding.ChangDuanTabListBinding;
import me.xlgp.xiquzimu.model.ChangDuan;
import me.xlgp.xiquzimu.ui.tablist.LeftAdapter;
import me.xlgp.xiquzimu.ui.tablist.RightAdapter;
import me.xlgp.xiquzimu.ui.zimu.main.ZimuMainFragment;
import me.xlgp.xiquzimu.util.ChangDuanHelper;

public class ChangDuanTabListFragment extends Fragment {

    private ChangDuanViewModel mViewModel;
    private ChangDuanTabListBinding binding;
    private final String juZhong;
    private LeftAdapter leftAdapter = null;
    private RightAdapter rightAdapter = null;

    public ChangDuanTabListFragment() {
        this.juZhong = "";
    }

    public ChangDuanTabListFragment(String juZhong) {
        this.juZhong = juZhong;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ChangDuanTabListBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(ChangDuanViewModel.class);
        mViewModel.setJuZhong(juZhong);

        leftAdapter = new LeftAdapter();
        binding.leftRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.leftRecyclerView.setAdapter(leftAdapter);
        leftAdapter.setOnItemClickListener((itemView, view, data, position) -> {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) binding.rightRecyclerView.getLayoutManager();
            if (linearLayoutManager != null) {
                linearLayoutManager.scrollToPositionWithOffset(data.getId(), 0);
            } else {
                binding.rightRecyclerView.smoothScrollToPosition(data.getId());
            }
        });

        rightAdapter = new RightAdapter();
        rightAdapter.setOnItemClickListener(getOnItemClickListener());
        binding.rightRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rightRecyclerView.setAdapter(rightAdapter);

        binding.zimuListSwipeRefreshLayout.setOnRefreshListener(() -> mViewModel.loadChangDuan(juZhong));


        mViewModel.loading.observe(getViewLifecycleOwner(), aBoolean -> binding.zimuListSwipeRefreshLayout.setRefreshing(aBoolean));
        mViewModel.state.observe(getViewLifecycleOwner(), s -> {
            if (s != null) Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
        });
        mViewModel.getChangDuanList().observe(getViewLifecycleOwner(), changDuanList -> {
            leftAdapter.updateData(ChangDuanHelper.setChangDuan(changDuanList));
            rightAdapter.updateData(changDuanList);
        });

        return binding.getRoot();
    }

    private BaseAdapter.OnItemClickListener<ChangDuan> getOnItemClickListener() {
        return (itemView, view, data, position) -> {
            ZimuMainFragment fragment = (ZimuMainFragment) getParentFragment();
            if (fragment != null) {
                fragment.forSkip(data);
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}