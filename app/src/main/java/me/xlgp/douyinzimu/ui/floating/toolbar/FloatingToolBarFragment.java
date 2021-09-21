package me.xlgp.douyinzimu.ui.floating.toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.databinding.FloatingToolBarFragmentBinding;
import me.xlgp.douyinzimu.listener.FloatingMoveListener;
import me.xlgp.douyinzimu.listener.OnDoubleClickListener;
import me.xlgp.douyinzimu.service.DianZanService;
import me.xlgp.douyinzimu.service.FloatingService;

public class FloatingToolBarFragment extends Fragment {

    private FloatingToolBarViewModel mViewModel;
    private FloatingToolBarFragmentBinding binding;
    private String title;
    private boolean isShou = true;
    private Integer shouHeight = 0;
    private LinearLayout rootLayout;

    public static FloatingToolBarFragment newInstance() {
        return new FloatingToolBarFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FloatingToolBarFragmentBinding.inflate(inflater, container, false);
        rootLayout = (LinearLayout) ((FloatingService) requireContext()).getRootView();
        binding.titleBtn.setText(title);
        onViewListener();
        return binding.getRoot();
    }

    public void setPanelTitle(String panelTitle) {
        title = panelTitle;
    }

    private int getShouHeight() {
        if (shouHeight > 0) return shouHeight;
        else {
            shouHeight = getPanelTitleHeight();
        }
        return shouHeight;
    }

    public int getPanelTitleHeight() {

        int count = rootLayout.getChildCount();
        int totalHeight = 0;
        for (int i = 1; i < count; i++) {
            totalHeight += rootLayout.getChildAt(i).getHeight();
        }
        return rootLayout.getHeight() - totalHeight;
    }

    private void updateViewLayout() {
        rootLayout.getLayoutParams().height = isShou ? WindowManager.LayoutParams.WRAP_CONTENT : getShouHeight();
        ((WindowManager) requireContext().getSystemService(Context.WINDOW_SERVICE))
                .updateViewLayout(rootLayout, rootLayout.getLayoutParams());
    }

    private void updatekaiOrShouBtn() {
        binding.kaiOrShouBtn.setImageResource(isShou ? R.drawable.ic_baseline_arrow_drop_up_24 : R.drawable.ic_baseline_arrow_drop_down_24);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void onViewListener() {
        binding.titleBtn.setOnTouchListener(new FloatingMoveListener(rootLayout,
                (WindowManager.LayoutParams) rootLayout.getLayoutParams(),
                (WindowManager) requireContext().getSystemService(Context.WINDOW_SERVICE)));

        binding.kaiOrShouBtn.setOnClickListener(v -> {
            isShou = !isShou;
            updateViewLayout();
            updatekaiOrShouBtn();
        });

        binding.titleBtn.setOnClickListener(new OnDoubleClickListener() {
            @Override
            public void doubleClick(View v) {
                new DianZanService().dianZan();
            }
        });

        binding.closeFloatingBtn.setOnClickListener(v -> {
            FloatingService service = (FloatingService) requireContext();
            service.stop();
        });
    }

    public static class Factory {
        public static FloatingToolBarFragment create() {
            FloatingToolBarFragment floatingToolBarFragment = FloatingToolBarFragment.newInstance();
            floatingToolBarFragment.setPanelTitle("唱段列表");
            return floatingToolBarFragment;
        }
    }
}