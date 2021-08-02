package me.xlgp.douyinzimu.view;

import android.content.Context;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.listener.FloatingMoveListener;
import me.xlgp.douyinzimu.service.FloatingService;

/**
 * 基本面板
 */
public class BasePenalLayout extends BaseFloatingLayout<LinearLayout> {
    private LinearLayout rootLayout;
    private boolean isShou = false;
    private int shouHeight;
    private String panelTitle;
    private int resource;

    public BasePenalLayout(@NonNull Context context, int resource, WindowManager.LayoutParams layoutParams) {
        super(context, R.layout.base_panel_layout, layoutParams);
        this.resource = resource;
    }

    public BasePenalLayout(Context context, int resource) {
        this(context, resource, null);
    }

    private void init() {
        this.rootLayout = (LinearLayout) getCurrentLayout();
        rootLayout.addView(inflateLayout(resource));
        onViewListener();
        shouHeight = 0;
    }

    @Override
    protected void build(WindowManager.LayoutParams layoutParams, String key) {
        super.build(layoutParams, key);
        init();
    }

    public String getPanelTitle() {
        return panelTitle;
    }

    public void setPanelTitle(String panelTitle) {
        this.panelTitle = panelTitle == null ? "悬浮窗口" : panelTitle;
    }

    private int getShouHeight() {
        if (shouHeight > 0) return shouHeight;
        else {
            shouHeight = getPanelTitleHeight();
        }
        return shouHeight;
    }

    public void setShouHeight(int shouHeight) {
        this.shouHeight = shouHeight;
    }

    public int getPanelTitleHeight() {
        int count = rootLayout.getChildCount();
        int totalHeight = 0;
        for (int i = 1; i < count; i++) {
            totalHeight += rootLayout.getChildAt(i).getHeight();
        }
        return rootLayout.getHeight() - totalHeight;
    }

    private void onViewListener() {
        rootLayout.findViewById(R.id.titleBtn).setOnTouchListener(new FloatingMoveListener(rootLayout, (WindowManager.LayoutParams) rootLayout.getLayoutParams(), (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)));
        this.rootLayout.findViewById(R.id.closeFloatingBtn).setOnClickListener(v -> {
            FloatingService service = (FloatingService) getContext();
            service.closeFloatingWindow(rootLayout);
        });
        this.rootLayout.findViewById(R.id.kaiOrShouBtn).setOnClickListener(v -> {
            if (isShou) {
                rootLayout.getLayoutParams().height = WindowManager.LayoutParams.WRAP_CONTENT;
                isShou = false;
            } else {
                rootLayout.getLayoutParams().height = getShouHeight();
                isShou = true;
            }
            ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).updateViewLayout(rootLayout, getLayoutParams());
        });
    }

}
