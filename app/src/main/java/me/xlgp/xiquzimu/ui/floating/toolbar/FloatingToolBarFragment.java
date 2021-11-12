package me.xlgp.xiquzimu.ui.floating.toolbar;

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

import me.xlgp.xiquzimu.R;
import me.xlgp.xiquzimu.databinding.FloatingToolBarFragmentBinding;
import me.xlgp.xiquzimu.listener.FloatingMoveListener;
import me.xlgp.xiquzimu.listener.OnDoubleClickListener;
import me.xlgp.xiquzimu.listener.OnFragmentChangeListener;
import me.xlgp.xiquzimu.service.DianZanService;
import me.xlgp.xiquzimu.service.FloatingService;

public class FloatingToolBarFragment extends Fragment {

    private FloatingToolBarFragmentBinding binding;
    private boolean isShou = true;
    private Integer shouHeight = 0;
    private LinearLayout rootLayout;
    private OnFragmentChangeListener onFragmentChangeListener;

    public static FloatingToolBarFragment newInstance() {
        return new FloatingToolBarFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FloatingToolBarFragmentBinding.inflate(inflater, container, false);
        rootLayout = (LinearLayout) ((FloatingService) requireContext()).getRootView();
        onViewListener();
        return binding.getRoot();
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

        binding.changciBtn.setOnClickListener(v -> {
            if (onFragmentChangeListener != null) {
                onFragmentChangeListener.onChange(true);
            }
        });
        binding.changduanBtn.setOnClickListener(v -> {
            if (onFragmentChangeListener != null) {
                onFragmentChangeListener.onChange(false);
            }
        });
    }

    public void setOnFragmentChangeListener(OnFragmentChangeListener onFragmentChangeListener) {
        this.onFragmentChangeListener = onFragmentChangeListener;
    }
}