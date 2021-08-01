package me.xlgp.douyinzimu.obj;

import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ZWindowManager implements WindowManager {
    WindowManager windowManager;
    Map<String, View> viewMap;
    private static ZWindowManager instance;

    private ZWindowManager() {
    }

    /**
     * @param windowManager 可以为空，但是第一次不能为空，
     * @return
     */
    public static ZWindowManager getInstance(WindowManager windowManager) {
        if (instance == null) {
            instance = new ZWindowManager();
            instance.windowManager = windowManager;
        }
        if (instance.viewMap == null) {
            instance.viewMap = new HashMap<>();
        }
        return instance;
    }

    @Override
    public Display getDefaultDisplay() {
        return windowManager.getDefaultDisplay();
    }

    @Override
    public void removeViewImmediate(View view) {
        windowManager.removeViewImmediate(view);
    }

    @Override
    public void addView(View view, ViewGroup.LayoutParams params) {
        windowManager.addView(view, params);
    }

    public void addView(View view, ViewGroup.LayoutParams params, String key) {
        try {
            addView(view, params);
            viewMap.put(key, view);
        } catch (Exception e) {
        }

    }

    @Override
    public void updateViewLayout(View view, ViewGroup.LayoutParams params) {
        windowManager.updateViewLayout(view, params);
    }

    @Override
    public void removeView(View view) {
        windowManager.removeView(view);
        Collection<View> collection = viewMap.values();
        collection.remove(view);
        windowManager.removeView(view);

    }

    public void removeView(String key) {
        View view = viewMap.get(key);
        if (view != null) {
            removeView(view);
        }
    }

    public void removeAllView() {
        for (Map.Entry<String, View> entry : viewMap.entrySet()) {
            windowManager.removeView(entry.getValue());
        }
        viewMap.clear();
    }
}
