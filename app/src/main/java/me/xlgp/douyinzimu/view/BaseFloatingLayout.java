package me.xlgp.douyinzimu.view;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowMetrics;

import androidx.annotation.RequiresApi;

import me.xlgp.douyinzimu.obj.ZWindowManager;

public class BaseFloatingLayout<T extends View> extends View {
    private View currentLayout;
    private String layoutName;
    private WindowManager.LayoutParams layoutParams;

    public BaseFloatingLayout(Context context) {
        this(context, null, null);
    }

    public BaseFloatingLayout(Context context, Integer resource) {
        this(context, resource, null);
    }

    public BaseFloatingLayout(Context context, Integer resource, WindowManager.LayoutParams layoutParams) {
        super(context);
        inflateLayout(resource);
        this.layoutParams = layoutParams;
    }

    protected View inflateLayout(int resource) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        currentLayout = inflater.inflate(resource, null);
        return currentLayout;
    }

    public View getCurrentLayout() {
        return currentLayout;
    }

    protected void build(WindowManager.LayoutParams layoutParams, String key) {
        ZWindowManager.getInstance(getContext()).addView(currentLayout, layoutParams, key);
        this.layoutName = key;
        this.layoutParams = layoutParams;
    }

    protected void build(WindowManager.LayoutParams layoutParams) {
        build(layoutParams, "floatinglayout" + ZWindowManager.getInstance(getContext()).count());
    }

    @Override
    public WindowManager.LayoutParams getLayoutParams() {
        return layoutParams;
    }

    public String getLayoutName() {
        return layoutName;
    }

    /**
     * 获取屏幕width
     *
     * @return
     */
    @RequiresApi(Build.VERSION_CODES.R)
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
