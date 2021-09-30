package me.xlgp.xiquzimu.obj;

import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ZWindowManager {
    private static ZWindowManager instance;
    WindowManager windowManager;
    Map<String, View> viewMap;

    private ZWindowManager() {
        this.viewMap = new HashMap<>();
    }

    public static ZWindowManager getInstance() {
        if (instance == null) {
            synchronized (ZWindowManager.class) {
                if (instance == null) {
                    instance = new ZWindowManager();
                }
            }
        }
        return instance;
    }

    public void build(WindowManager windowManager) {
        this.windowManager = windowManager;
    }

    public void addView(View view, ViewGroup.LayoutParams params) {
        addView(view, params, "floatinglayout" + ZWindowManager.getInstance().count());
    }

    public int count() {
        return viewMap.size();
    }

    public void addView(View view, ViewGroup.LayoutParams params, String key) {
        windowManager.addView(view, params);
        viewMap.put(key, view);
    }

    public boolean containView(String key) {
        return viewMap.containsKey(key);
    }

    public boolean containView(View view) {
        return viewMap.containsValue(view);
    }

    public void removeView(View view) {
        windowManager.removeView(view);
        Collection<View> collection = viewMap.values();
        collection.remove(view);

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
