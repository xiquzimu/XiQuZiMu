package me.xlgp.xiquzimu.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import me.xlgp.xiquzimu.databinding.FragmentDashboardBinding;
import me.xlgp.xiquzimu.predicate.ChangDuanPredicate;
import me.xlgp.xiquzimu.ui.changci.ChangCiActivity;
import me.xlgp.xiquzimu.view.ChangDuanSearchRecyclerviewLayout;

public class DashboardFragment extends Fragment {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private FragmentDashboardBinding binding;
    private ChangDuanSearchRecyclerviewLayout searchRecyclerviewLayout;
    private ChangDuanViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        searchRecyclerviewLayout = binding.zimuListSearchRecyclerviewLayout;

        viewModel = new ViewModelProvider(this).get(ChangDuanViewModel.class);
        viewModel.deleteState.observe(getViewLifecycleOwner(), s -> Snackbar.make(requireView(), s, Snackbar.LENGTH_SHORT).show());
        viewModel.loadState.observe(getViewLifecycleOwner(), s -> Snackbar.make(requireView(), s, Snackbar.LENGTH_SHORT).show());
        initSearchRecyclerviewLayout();

        ChangDuanListAdapter changDuanListAdapter = new ChangDuanListAdapter();
        changDuanListAdapter.setOnItemClickListener((itemView, view, data, position) -> toChangCiActivity(data.getId()));
        searchRecyclerviewLayout.setSearchListAdapter(changDuanListAdapter);
        searchRecyclerviewLayout.getRecyclerview().addItemDecoration(new ChangDuanListAdapter.GroupHeaderItemDecoration(changDuanListAdapter));
        viewModel.getChangduanList().observe(getViewLifecycleOwner(), list -> {
            searchRecyclerviewLayout.setRefreshing(false);
            if (list.size() == 0) {
                Toast.makeText(requireContext(), "????????????", Toast.LENGTH_SHORT).show();
            }
            setTotalCountTextView(list.size());
            changDuanListAdapter.updateData(list);
        });

        binding.clear.setOnClickListener(this::onClearList);
        binding.update.setOnClickListener(this::onFetch);
        binding.add.setOnClickListener(this::add);
        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        searchRecyclerviewLayout.clearFocus();
    }

    private void toChangCiActivity(Integer id) {
        Intent intent = new Intent(requireActivity(), ChangCiActivity.class);
        intent.putExtra("changduanID", id);
        startActivity(intent);
    }

    private void setTotalCountTextView(int count) {
        String text = "?????? " + count + " ???";
        binding.totalCountTextView.setText(text);
    }

    private void initSearchRecyclerviewLayout() {

        searchRecyclerviewLayout.build(this);
        searchRecyclerviewLayout.setRefreshing(true);
        searchRecyclerviewLayout.setPredicate(new ChangDuanPredicate(searchRecyclerviewLayout.getFilterCharSequenceLiveData()));
        searchRecyclerviewLayout.setOnRefreshListener(viewModel::loadChangDuanList);
    }

    public void add(View view) {
        toChangCiActivity(-1);
    }

    public void onFetch(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("????????????")
                .setMessage("???????????????????")
                .setPositiveButton("??????", (dialog, which) -> {
                    searchRecyclerviewLayout.setRefreshing(true);
                    compositeDisposable.add(viewModel.fetchChangDuanList().subscribe(id -> {
                    }, throwable -> Toast.makeText(requireContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show()));
                })
                .setNegativeButton("??????", (dialog, which) -> {
                }).show();

    }

    public void onClearList(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("??????")
                .setMessage("?????????????????????")
                .setPositiveButton("??????", (dialog, which) -> viewModel.deleteChangDuanList())
                .setNegativeButton("??????", (dialog, which) -> {
                }).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}