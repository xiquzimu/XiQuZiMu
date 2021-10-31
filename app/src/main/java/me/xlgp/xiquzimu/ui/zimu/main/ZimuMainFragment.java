package me.xlgp.xiquzimu.ui.zimu.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

import me.xlgp.xiquzimu.databinding.ZimuMainFragmentBinding;
import me.xlgp.xiquzimu.listener.OnFragmentChangeListener;
import me.xlgp.xiquzimu.model.ChangDuan;
import me.xlgp.xiquzimu.service.PinglunLifecycleService;
import me.xlgp.xiquzimu.ui.zimu.changduan.ChangDuanTabListFragment;

public class ZimuMainFragment extends Fragment {

    private ZimuMainFragmentBinding binding;
    private Intent intent = null;
    private OnFragmentChangeListener onFragmentChangeListener;

    public static ZimuMainFragment newInstance() {
        return new ZimuMainFragment();
    }

    public void forSkip(ChangDuan changDuan) {
        intent = new Intent(requireContext(), PinglunLifecycleService.class);
        intent.putExtra(PinglunLifecycleService.CHANG_DUAN_ID, changDuan.getId());
        requireContext().startService(intent);
        onFragmentChangeListener.onChange(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ZimuMainFragmentBinding.inflate(inflater, container, false);
        ZimuMainViewModel viewModel = new ViewModelProvider(this).get(ZimuMainViewModel.class);
        viewModel.getTabNameList().observe(getViewLifecycleOwner(), list -> {
            binding.zimuViewpager2.setAdapter(new ZimuMainStateAdapter(this, list));
            initTabList(list);
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (intent != null) {
            requireContext().stopService(intent);
        }
    }

    private void initTabList(List<String> list) {
        list.forEach(name -> {
            TabLayout.Tab tab = binding.zimuTabList.newTab();
            tab.setText(name);
            binding.zimuTabList.addTab(tab);
        });
        new TabLayoutMediator(binding.zimuTabList, binding.zimuViewpager2,
                (tab, position) -> tab.setText(list.get(position))).attach();
    }

    public void setOnFragmentChangeListener(OnFragmentChangeListener onFragmentChangeListener) {
        this.onFragmentChangeListener = onFragmentChangeListener;
    }

    static class ZimuMainStateAdapter extends FragmentStateAdapter {

        private final Fragment[] fragments;
        private final List<String> list;

        public ZimuMainStateAdapter(@NonNull Fragment fragment, List<String> list) {
            super(fragment);
            fragments = new Fragment[list.size()];
            this.list = list;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (fragments[position] == null) {
                fragments[position] = new ChangDuanTabListFragment(list.get(position));
            }
            return fragments[position];
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}
