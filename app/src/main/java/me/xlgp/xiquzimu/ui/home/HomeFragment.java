package me.xlgp.xiquzimu.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import io.reactivex.rxjava3.core.Observable;
import me.xlgp.xiquzimu.constant.UrlConstant;
import me.xlgp.xiquzimu.databinding.FragmentHomeBinding;
import me.xlgp.xiquzimu.designpatterns.ObserverHelper;
import me.xlgp.xiquzimu.service.FloatingService;
import me.xlgp.xiquzimu.ui.fetchlist.ListActivity;
import me.xlgp.xiquzimu.ui.webview.GiteeWebActivity;
import me.xlgp.xiquzimu.util.AccessibilitySettingsHelper;
import me.xlgp.xiquzimu.util.FloatingHelper;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private final ActivityResultLauncher<Intent> floatingLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> judgeFloating());
    private final ActivityResultLauncher<Intent> accessibilityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> judgeAccessibility());
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        homeViewModel.getAccessibilitySettingStatus().observe(requireActivity(), result -> {
            QuanxianState quanxianState = result.getData();
            binding.openAccessibilitySetting.setText(quanxianState.getTextid());
            binding.openAccessibilitySetting.setTextColor(requireContext().getResources().getColor(quanxianState.getResouceId(), null));
        });
        homeViewModel.getFloatQuanxianState().observe(requireActivity(), result -> {
            QuanxianState quanxianState = result.getData();
            binding.openFloatingServiceBtn.setTextColor(requireContext().getResources().getColor(quanxianState.getResouceId(), null));
            binding.openFloatingServiceBtn.setText(quanxianState.getTextid());
        });

        binding.runBtn.setOnClickListener(this::onRun);
        binding.openAccessibilitySetting.setOnClickListener(this::onOpenAccessibilitySetting);
        binding.openFloatingServiceBtn.setOnClickListener(this::onStartFloatingService);

        binding.remoteBtn.setOnClickListener(this::onRemote);
        binding.createLRCBtn.setOnClickListener(this::onCreateLRC);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        judgeFloating();
        judgeAccessibility();
    }

    private void judgeFloating() {
        if (FloatingHelper.enable(requireContext())) {
            homeViewModel.getFloatQuanxianState().setValue(new FloatQuanxianState.Succes());
        } else {
            homeViewModel.getFloatQuanxianState().setValue(new FloatQuanxianState.Error());
        }
    }

    private void judgeAccessibility() {
        if (AccessibilitySettingsHelper.isEnabled(requireContext())) {
            homeViewModel.getAccessibilitySettingStatus().setValue(new AccessibilitySettingStatus.Succes());
        } else {
            homeViewModel.getAccessibilitySettingStatus().setValue(new AccessibilitySettingStatus.Error());
        }
    }

    public void onCreateLRC(View view) {
        Intent intent = new Intent(requireContext(), GiteeWebActivity.class);
        intent.putExtra("URL", UrlConstant.CHANG_CI);
        startActivity(intent);
    }

    public void onRemote(View view) {
        startActivity(new Intent(requireActivity(), ListActivity.class));
    }

    public void onStartFloatingService(View view) {
        if (!FloatingHelper.enable(requireContext())) {
            floatingLauncher.launch(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + requireActivity().getPackageName())));
            return;
        }
        Toast.makeText(requireContext(), "已开启悬浮权限", Toast.LENGTH_SHORT).show();
    }

    public void onOpenAccessibilitySetting(View view) {
        if (!AccessibilitySettingsHelper.isEnabled(requireContext())) {
            accessibilityLauncher.launch(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            return;
        }
        Toast.makeText(requireContext(), "已开启无障碍服务", Toast.LENGTH_SHORT).show();
    }

    public void onRun(View view) {
        Context context = requireContext();
        if (!AccessibilitySettingsHelper.isEnabled(context)) {
            Toast.makeText(context, "请先开启无障碍服务", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!FloatingHelper.enable(context)) {
            Toast.makeText(context, "请开启悬浮权限", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent floatingIntent = new Intent(getActivity(), FloatingService.class);

        Observable.create(emitter -> emitter.onNext(context.startService(floatingIntent)))
                .compose(ObserverHelper.transformer()).subscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}