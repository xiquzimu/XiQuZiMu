package me.xlgp.douyinzimu.view;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowMetrics;

import me.xlgp.douyinzimu.obj.ZWindowManager;


public class BaseFloatingLayout extends View {
    private View currentLayout;
    private String layoutName;

    public BaseFloatingLayout(Context context) {
        super(context);
    }

    public BaseFloatingLayout(Context context, int resource) {
        this(context);
        inflateLayout(resource);
    }

    protected View inflateLayout(int resource) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        currentLayout = inflater.inflate(resource, null);
        return currentLayout;
    }

    public View getCurrentLayout() {
        return currentLayout;
    }

    protected void addViewToWindowManager(WindowManager.LayoutParams layoutParams, String key) {
        ZWindowManager.getInstance(getContext()).addView(currentLayout, layoutParams, key);
        this.layoutName = key;
    }

    protected void addViewToWindowManager(WindowManager.LayoutParams layoutParams) {
        addViewToWindowManager(layoutParams, "floatinglayout" + ZWindowManager.getInstance(getContext()).count());
    }

    public String getLayoutName() {
        return layoutName;
    }

    /**
     * 获取屏幕width
     *
     * @return
     */
    protected int getFullWidth() {
        int width;
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = windowManager.getCurrentWindowMetrics();
            width = windowMetrics.getBounds().width();
        } else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            width = displayMetrics.widthPixels;
        }
        return width;
    }
}
