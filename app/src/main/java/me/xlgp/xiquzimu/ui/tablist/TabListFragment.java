package me.xlgp.xiquzimu.ui.tablist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.jetbrains.annotations.NotNull;

import me.xlgp.xiquzimu.databinding.FragmentTabListBinding;
import me.xlgp.xiquzimu.ui.dashboard.ChangDuanListAdapter;
import me.xlgp.xiquzimu.ui.dashboard.ChangDuanViewModel;
import me.xlgp.xiquzimu.util.ChangDuanHelper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabListFragment extends Fragment {

    private FragmentTabListBinding binding;
    private ChangDuanViewModel viewModel;
    private LeftAdapter leftAdapter = null;
    private ChangDuanListAdapter changDuanListAdapter = null;

    public TabListFragment() {
    }

    public static TabListFragment newInstance() {
        return new TabListFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTabListBinding.inflate(inflater, container, false);

        leftAdapter = new LeftAdapter();
        binding.leftRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.leftRecyclerView.setAdapter(leftAdapter);

        changDuanListAdapter = new ChangDuanListAdapter();
        binding.rightRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rightRecyclerView.setAdapter(changDuanListAdapter);

        viewModel = new ViewModelProvider(this).get(ChangDuanViewModel.class);

        viewModel.getChangduanList().observe(getViewLifecycleOwner(), changDuanList -> {
            leftAdapter.updateData(ChangDuanHelper.setChangDuan(changDuanList));
            changDuanListAdapter.updateData(changDuanList);
        });

        leftAdapter.setOnItemClickListener((itemView, view, data, position) -> {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) binding.rightRecyclerView.getLayoutManager();
            if (linearLayoutManager != null) {
                linearLayoutManager.scrollToPositionWithOffset(data.getId(), 0);
            } else {
                binding.rightRecyclerView.smoothScrollToPosition(data.getId());
            }

        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}