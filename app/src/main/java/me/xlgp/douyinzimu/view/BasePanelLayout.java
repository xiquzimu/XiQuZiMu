package me.xlgp.douyinzimu.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.databinding.BasePanelLayoutBinding;
import me.xlgp.douyinzimu.listener.FloatingMoveListener;
import me.xlgp.douyinzimu.obj.ZWindowManager;

/**
 * 基本面板
 */
public class BasePanelLayout extends BaseFloatingLayout implements OnCloseListener {
    private final int resource;

    private LinearLayout rootLayout;
    private boolean isShou = true;
    private int shouHeight;
    private BasePanelLayoutBinding binding;

    public BasePanelLayout(@NonNull Context context, int resource, WindowManager.LayoutParams layoutParams) {
        super(context, R.layout.base_panel_layout, layoutParams);
        this.resource = resource;
    }

    public BasePanelLayout(Context context, int resource) {
        this(context, resource, null);
    }

    private void init() {
        binding = BasePanelLayoutBinding.bind(getCurrentLayout());

        rootLayout = binding.getRoot();
        rootLayout.addView(inflateLayout(resource));

        shouHeight = 0;
        onViewListener();
    }

    @Override
    protected void build(WindowManager.LayoutParams layoutParams, String key) {
        super.build(layoutParams, key);
        init();
    }

    public void setPanelTitle(String panelTitle) {
        String title = panelTitle == null ? "悬浮窗口" : panelTitle;
        binding.titleBtn.setText(title);
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

    public void setOnTitleClickListener(View.OnClickListener onClickListener) {
        binding.titleBtn.setOnClickListener(onClickListener);
    }

    @Override
    public void onClose() {
        ZWindowManager.getInstance().removeView(rootLayout);
    }

    private void updateViewLayout() {
        rootLayout.getLayoutParams().height = isShou ? WindowManager.LayoutParams.WRAP_CONTENT : getShouHeight();
        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE))
                .updateViewLayout(rootLayout, rootLayout.getLayoutParams());
    }

    private void updatekaiOrShouBtn() {
        binding.kaiOrShouBtn.setImageResource(isShou ? R.drawable.ic_baseline_arrow_drop_up_24 : R.drawable.ic_baseline_arrow_drop_down_24);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void onViewListener() {
        binding.titleBtn.setOnTouchListener(new FloatingMoveListener(rootLayout,
                (WindowManager.LayoutParams) rootLayout.getLayoutParams(),
                (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)));

        binding.kaiOrShouBtn.setOnClickListener(v -> {
            isShou = !isShou;
            updateViewLayout();
            updatekaiOrShouBtn();
        });

        binding.closeFloatingBtn.setOnClickListener(v -> onClose());
    }
}

interface OnCloseListener {
    void onClose();
}
