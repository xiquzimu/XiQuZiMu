package me.xlgp.douyinzimu.ui.zimu.main;

import android.content.Intent;
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
import me.xlgp.douyinzimu.model.ChangDuan;
import me.xlgp.douyinzimu.service.PinglunLifecycleService;
import me.xlgp.douyinzimu.ui.zimu.changci.ChangCiFragment;
import me.xlgp.douyinzimu.ui.zimu.changduan.ChangDuanFragment;

public class ZimuMainFragment extends Fragment {

    private ZimuMainFragmentBinding binding;
    private OnSwitchFragmentListener onSwitchFragmentListener;
    String[] names = new String[]{"黄梅戏", "越剧","歌曲","小调", "唱词"};
    private Intent intent = null;

    public static ZimuMainFragment newInstance() {
        return new ZimuMainFragment();
    }

    public void setOnSwitchFragmentListener(OnSwitchFragmentListener onSwitchFragmentListener) {
        this.onSwitchFragmentListener = onSwitchFragmentListener;
    }

    public void forSkip(ChangDuan changDuan) {
        if (onSwitchFragmentListener != null) {
            intent = new Intent(requireContext(), PinglunLifecycleService.class);
            intent.putExtra(PinglunLifecycleService.CHANG_DUAN_ID, changDuan.getId());
            requireContext().startService(intent);
            onSwitchFragmentListener.onSwitch(binding.zimuViewpager2, names.length-1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ZimuMainFragmentBinding.inflate(inflater, container, false);
        binding.zimuViewpager2.setAdapter(new ZimuMainStateAdapter(this));
        initTabList();
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireContext().stopService(intent);
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
                if (position == names.length - 1) fragments[position] = createChangCiFragment();
                else fragments[position] = new ChangDuanFragment(names[position]);
            }
            return fragments[position];
        }

        private Fragment createChangCiFragment() {
            return ChangCiFragment.newInstance();
        }

        @Override
        public int getItemCount() {
            return names.length;
        }
    }
}
