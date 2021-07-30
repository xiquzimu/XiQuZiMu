package me.xlgp.douyinzimu.util;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.service.DianZanService;

public class DianZanHelper {
    private Context context;

    public DianZanHelper() {

    }

    public DianZanHelper(Context context) {
        this.context = context;
    }

    /**
     * 主要验证当前事件源是否是点赞按钮发出的
     *
     * @param event
     * @return
     */
    public static boolean check(Context context, AccessibilityEvent event) {
        AccessibilityNodeInfo src = event.getSource();
        String zan = context.getString(R.string.dianzan);
        if (src != null && zan.equals(src.getText())) {
            return true;
        }
        return false;
    }

    /**
     * 验证能否点赞，
     * 1：事件源是否是点赞按钮发出的，
     * 2：当前界面是否是douyin
     *
     * @param context
     * @param event
     * @return
     */
    public static boolean isEnabled(Context context, AccessibilityEvent event) {
        return check(context, event) && AppHelper.isDouYinWindows((AccessibilityService) context);
    }


    public static void dianZan(AccessibilityService service) {
        Observable.just("").map(o -> {
            new DianZanService(service).dianZan();
            return "";
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }
}
