package me.xlgp.douyinzimu.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import me.xlgp.douyinzimu.obj.ZWindowManager;

public class BaseFloatingLayout {
    private final Context context;
    private final View currentLayout;
    private String layoutName;

    public BaseFloatingLayout(Context context, Integer resource, WindowManager.LayoutParams layoutParams) {
        this.context = context;
        currentLayout = inflateLayout(resource);
    }

    protected View inflateLayout(int resource) {
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(resource, null);
    }

    public View getCurrentLayout() {
        return currentLayout;
    }

    public Context getContext() {
        return context;
    }

    protected void build(WindowManager.LayoutParams layoutParams, String key) {
        ZWindowManager.getInstance().addView(currentLayout, layoutParams, key);
        this.layoutName = key;
    }

    protected void build(WindowManager.LayoutParams layoutParams) {
        build(layoutParams, "floatinglayout" + ZWindowManager.getInstance().count());
    }

    public String getLayoutName() {
        return layoutName;
    }

}
