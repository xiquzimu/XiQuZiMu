package me.xlgp.xiquzimu.ui.about;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import me.xlgp.xiquzimu.R;
import me.xlgp.xiquzimu.constant.UrlConstant;
import me.xlgp.xiquzimu.databinding.FragmentAboutBinding;
import me.xlgp.xiquzimu.service.DownloadService;
import me.xlgp.xiquzimu.ui.webview.GiteeWebActivity;
import me.xlgp.xiquzimu.util.AnimationHelper;

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
            Intent intent = new Intent(requireActivity(), DownloadService.class);
            intent.putExtra(DownloadService.KEY_DOWNLOAD_URL, list.get(0));
            requireActivity().startService(intent);
            loading(false);
        });


        binding.updateVersion.setOnClickListener(v -> {
            loading(true);
            viewModel.loadDownloadUrl();
        });
        binding.statement.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), GiteeWebActivity.class);
            intent.putExtra(GiteeWebActivity.KEY_URL, UrlConstant.STATEMENT);
            requireActivity().startActivity(intent);
        });
        binding.projectUrl.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), GiteeWebActivity.class);
            intent.putExtra(GiteeWebActivity.KEY_URL, requireContext().getString(R.string.giteeProjectUrl));
            requireActivity().startActivity(intent);
        });
        return root;
    }


    private void loading(boolean loading) {
        if (loading) {
            binding.loadingImageView.setVisibility(View.VISIBLE);
            binding.loadingImageView.startAnimation(AnimationHelper.getRotateAnimation(requireContext(), R.anim.anim_circle_rotate));
        } else {
            binding.loadingImageView.clearAnimation();
            binding.loadingImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
