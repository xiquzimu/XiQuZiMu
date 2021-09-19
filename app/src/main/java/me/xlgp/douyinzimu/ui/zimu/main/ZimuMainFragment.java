package me.xlgp.douyinzimu.ui.zimu.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import me.xlgp.douyinzimu.databinding.ZimuMainFragmentBinding;
import me.xlgp.douyinzimu.listener.OnSwitchFragmentListener;
import me.xlgp.douyinzimu.ui.zimu.changci.ChangCiFragment;
import me.xlgp.douyinzimu.ui.zimu.changduan.ChangDuanFragment;

public class ZimuMainFragment extends Fragment {

    private ZimuMainFragmentBinding binding;
    private OnSwitchFragmentListener onSwitchFragmentListener;
    String[] names = new String[]{"黄梅戏", "唱词"};

    public static ZimuMainFragment newInstance() {
        return new ZimuMainFragment();
    }

    public void setOnSwitchFragmentListener(OnSwitchFragmentListener onSwitchFragmentListener) {
        this.onSwitchFragmentListener = onSwitchFragmentListener;
    }

    public void forSkip() {
        if (onSwitchFragmentListener != null) {
            onSwitchFragmentListener.onSwitch(binding.zimuViewpager2);
        }
    }

    public Integer getLastViewPager2Index() {
        return names.length - 1;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ZimuMainFragmentBinding.inflate(inflater, container, false);
        binding.zimuViewpager2.setAdapter(new ZimuMainStateAdapter(this));
        initTabList();
        return binding.getRoot();
    }

    private void initTabList() {

        for (String name : names) {
            TabLayout.Tab tab = binding.zimuTabList.newTab();
            tab.setText(name);
            binding.zimuTabList.addTab(tab);
        }
        new TabLayoutMediator(binding.zimuTabList, binding.zimuViewpager2,
                (tab, position) -> tab.setText(names[position])).attach();
    }

    class ZimuMainStateAdapter extends FragmentStateAdapter {

        private final Fragment[] fragments;

        public ZimuMainStateAdapter(@NonNull Fragment fragment) {
            super(fragment);
            fragments = new Fragment[names.length];
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (fragments[position] == null) {
                if (position == names.length - 1) return createChangCiFragment();
                return createChangDuanFragment();
            }
            return fragments[position];
        }

        private Fragment createChangDuanFragment() {
            return new ChangDuanFragment();
        }

        private Fragment createChangCiFragment() {
            return new ChangCiFragment();
        }

        @Override
        public int getItemCount() {
            return names.length;
        }
    }
}
