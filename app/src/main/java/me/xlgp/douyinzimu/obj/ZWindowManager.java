package me.xlgp.douyinzimu.obj;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ZWindowManager {
    WindowManager windowManager;
    Map<String, View> viewMap;
    private static ZWindowManager instance;

    private ZWindowManager(Context context) {
        this.windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        this.viewMap = new HashMap<>();
    }

    public static ZWindowManager getInstance(Context context) {
        if (instance == null) {
            instance = new ZWindowManager(context);
        }
        return instance;
    }

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
