package me.xlgp.xiquzimu.ui.about;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import me.xlgp.xiquzimu.constant.UrlConstant;
import me.xlgp.xiquzimu.databinding.FragmentAboutBinding;
import me.xlgp.xiquzimu.service.DownloadService;
import me.xlgp.xiquzimu.ui.webview.GiteeWebActivity;

public class AboutFragment extends Fragment {

    private FragmentAboutBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AboutViewModel viewModel = new ViewModelProvider(this).get(AboutViewModel.class);

        binding = FragmentAboutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewModel.getPackageVersion().observe(getViewLifecycleOwner(), s -> binding.textPackageVersion.setText(s));
        viewModel.errorState.observe(getViewLifecycleOwner(), s -> Snackbar.make(binding.getRoot(), s, Snackbar.LENGTH_SHORT).show());

        viewModel.getDownloadUrlList().observe(getViewLifecycleOwner(), list -> {
            Log.i("TAG", "onCreateView: " + list);
            Intent intent = new Intent(requireActivity(), DownloadService.class);
            intent.putExtra("apkDownloadUrl", list.get(0));
            requireActivity().startService(intent);
        });

        binding.updateVersion.setOnClickListener(v -> viewModel.loadDownloadUrl());
        binding.statement.setOnClickListener(v->{
            Intent intent = new Intent(requireContext(), GiteeWebActivity.class);
            intent.putExtra("URL", UrlConstant.STATEMENT);
            requireActivity().startActivity(intent);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
