package me.xlgp.xiquzimu.ui.about;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import me.xlgp.xiquzimu.databinding.FragmentAboutBinding;
import me.xlgp.xiquzimu.ui.webview.GiteeWebActivity;

public class AboutFragment extends Fragment {

    private FragmentAboutBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AboutViewModel aboutViewModel = new ViewModelProvider(this).get(AboutViewModel.class);

        binding = FragmentAboutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        aboutViewModel.getPackageVersion().observe(getViewLifecycleOwner(), s -> binding.textPackageVersion.setText(s));

        binding.updateVersion.setOnClickListener(v -> startActivity(new Intent(requireActivity(), GiteeWebActivity.class)));
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
