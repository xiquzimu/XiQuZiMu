package me.xlgp.douyinzimu.ui.zimu.changduan;

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

import me.xlgp.douyinzimu.adapter.BaseAdapter;
import me.xlgp.douyinzimu.databinding.ChangDuanFragmentBinding;
import me.xlgp.douyinzimu.model.ChangDuan;
import me.xlgp.douyinzimu.ui.zimu.ZimuViewModel;
import me.xlgp.douyinzimu.ui.zimu.main.ZimuMainFragment;

public class ChangDuanFragment extends Fragment {

    private ChangDuanViewModel mViewModel;
    private ChangDuanFragmentBinding binding;

    public static ChangDuanFragment newInstance() {
        return new ChangDuanFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ChangDuanFragmentBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(ChangDuanViewModel.class);

        ChangDuanAdapter changDuanAdapter = new ChangDuanAdapter();

        binding.zimuListSwipeRefreshLayout.setOnRefreshListener(() -> mViewModel.loadChangDuan());

        binding.zimuListRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        changDuanAdapter.setOnItemClickListener(getOnItemClickListener());
        binding.zimuListRecyclerview.setAdapter(changDuanAdapter);

        mViewModel.loading.observe(getViewLifecycleOwner(), aBoolean -> binding.zimuListSwipeRefreshLayout.setRefreshing(aBoolean));
        mViewModel.state.observe(getViewLifecycleOwner(), s -> {
            if (s != null) Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
        });
        mViewModel.getChangDuanList().observe(getViewLifecycleOwner(), changDuanAdapter::updateData);

        return binding.getRoot();
    }

    private BaseAdapter.OnItemClickListener<ChangDuan> getOnItemClickListener() {
        return (itemView, view, data, position) -> {
            ZimuViewModel.getChangDuan().postValue(data);
            ZimuMainFragment fragment = (ZimuMainFragment) getParentFragment();
            if (fragment != null) {
                fragment.setOnSwitchFragmentListener(viewPager2 -> viewPager2.setCurrentItem(fragment.getLastViewPager2Index()));
                fragment.forSkip();
            }
        };
    }
}