package me.xlgp.douyinzimu.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import me.xlgp.douyinzimu.adapter.ChangDuanListAdapter;
import me.xlgp.douyinzimu.databinding.FragmentDashboardBinding;
import me.xlgp.douyinzimu.model.ChangDuan;
import me.xlgp.douyinzimu.predicate.ChangDuanPredicate;
import me.xlgp.douyinzimu.service.ChangCiService;
import me.xlgp.douyinzimu.service.ChangDuanService;
import me.xlgp.douyinzimu.ui.main.SearchRecyclerviewLayout;
import me.xlgp.douyinzimu.viewmodel.ChangDuanViewModel;

public class DashboardFragment extends Fragment {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    private SearchRecyclerviewLayout<ChangDuan> searchRecyclerviewLayout;
    private ChangDuanViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        searchRecyclerviewLayout = binding.zimuListSearchRecyclerviewLayout;

        viewModel = new ViewModelProvider(this).get(ChangDuanViewModel.class);
        initSearchRecyclerviewLayout();

        ChangDuanListAdapter changDuanListAdapter = new ChangDuanListAdapter();
        searchRecyclerviewLayout.setSearchListAdapter(changDuanListAdapter);
        viewModel.getChangduanList().observe(getViewLifecycleOwner(), list -> {
            searchRecyclerviewLayout.setRefreshing(false);
            if (list.size() == 0) {
                Toast.makeText(requireContext(), "没有唱词", Toast.LENGTH_SHORT).show();
                return;
            }
            changDuanListAdapter.updateData(list);
        });

        binding.clear.setOnClickListener(this::onClearList);
        binding.update.setOnClickListener(this::onFetch);

        return root;
    }

    private void initSearchRecyclerviewLayout() {

        searchRecyclerviewLayout.build(this);
        searchRecyclerviewLayout.setRefreshing(true);
        searchRecyclerviewLayout.setPredicate(new ChangDuanPredicate(searchRecyclerviewLayout.getFilterCharSequenceLiveData()));
        searchRecyclerviewLayout.setOnRefreshListener(viewModel::loadChangDuanList);
    }

    public void onFetch(View view) {
        searchRecyclerviewLayout.setRefreshing(true);
        viewModel.fetchChangDuanList();
    }

    public void onClearList(View view) {
        try {
            new ChangDuanService(compositeDisposable).deleteAll();
            Toast.makeText(requireContext(), "唱段删除完毕", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(requireContext(), "唱段删除异常", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        try {
            new ChangCiService(compositeDisposable).deleteAll();
            Toast.makeText(requireContext(), "唱词删除完毕", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(requireContext(), "删除唱词异常", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}