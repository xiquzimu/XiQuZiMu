package me.xlgp.douyinzimu.ui.dashboard;

import android.annotation.SuppressLint;
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
import me.xlgp.douyinzimu.view.SearchRecyclerviewLayout;

public class DashboardFragment extends Fragment {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private FragmentDashboardBinding binding;
    private SearchRecyclerviewLayout<ChangDuan> searchRecyclerviewLayout;
    private ChangDuanViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        searchRecyclerviewLayout = binding.zimuListSearchRecyclerviewLayout;

        viewModel = new ViewModelProvider(this).get(ChangDuanViewModel.class);
        viewModel.deleteState.observe(getViewLifecycleOwner(), s -> {
            if (s != null) {
                Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
            }
        });
        initSearchRecyclerviewLayout();

        ChangDuanListAdapter changDuanListAdapter = new ChangDuanListAdapter();
        searchRecyclerviewLayout.setSearchListAdapter(changDuanListAdapter);
        viewModel.getChangduanList().observe(getViewLifecycleOwner(), list -> {
            searchRecyclerviewLayout.setRefreshing(false);
            if (list.size() == 0) {
                Toast.makeText(requireContext(), "没有唱词", Toast.LENGTH_SHORT).show();
            }
            setTotalCountTextView(list.size());
            changDuanListAdapter.updateData(list);
        });

        binding.clear.setOnClickListener(this::onClearList);
        binding.update.setOnClickListener(this::onFetch);

        return root;
    }

    @SuppressLint("SetTextI18n")
    private void setTotalCountTextView(int count) {
        binding.totalCountTextView.setText("共有唱段 " + count + " 段");
    }

    private void initSearchRecyclerviewLayout() {

        searchRecyclerviewLayout.build(this);
        searchRecyclerviewLayout.setRefreshing(true);
        searchRecyclerviewLayout.setPredicate(new ChangDuanPredicate(searchRecyclerviewLayout.getFilterCharSequenceLiveData()));
        searchRecyclerviewLayout.setOnRefreshListener(viewModel::loadChangDuanList);
    }

    public void onFetch(View view) {
        searchRecyclerviewLayout.setRefreshing(true);
        compositeDisposable.add(viewModel.fetchChangDuanList().subscribe(id -> {
        }, throwable -> Toast.makeText(requireContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show()));
    }

    public void onClearList(View view) {
        viewModel.deleteChangDuanList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}