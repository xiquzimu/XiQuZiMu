package me.xlgp.douyinzimu.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import io.reactivex.rxjava3.core.Observable;
import me.xlgp.douyinzimu.ListActivity;
import me.xlgp.douyinzimu.databinding.FragmentHomeBinding;
import me.xlgp.douyinzimu.designpatterns.ObserverHelper;
import me.xlgp.douyinzimu.service.FloatingService;
import me.xlgp.douyinzimu.util.AccessibilitySettingsHelper;
import me.xlgp.douyinzimu.util.FloatingHelper;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private Button openFloatingBtn = null;
    private final ActivityResultLauncher<Intent> floatingLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> FloatingHelper.updateFloatingBtn(requireContext(), openFloatingBtn));
    private Button openAccessibilitySettingBtn = null;
    private final ActivityResultLauncher<Intent> accessibilityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> AccessibilitySettingsHelper.updateAccessibilitySettingBtn(requireContext(), openAccessibilitySettingBtn));

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        openFloatingBtn = binding.openFloatingServiceBtn;
        openAccessibilitySettingBtn = binding.openAccessibilitySetting;

        AccessibilitySettingsHelper.updateAccessibilitySettingBtn(requireContext(), openAccessibilitySettingBtn);
        FloatingHelper.updateFloatingBtn(requireContext(), openFloatingBtn);

        binding.runBtn.setOnClickListener(this::onRun);
        binding.openAccessibilitySetting.setOnClickListener(this::onOpenAccessibilitySetting);
        binding.openFloatingServiceBtn.setOnClickListener(this::onStartFloatingService);

        binding.remoteBtn.setOnClickListener(this::onRemote);

        return root;
    }

    public void onRemote(View view){
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
        Observable.create(emitter -> emitter.onNext(context.startService(floatingIntent))).compose(ObserverHelper.transformer()).subscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}