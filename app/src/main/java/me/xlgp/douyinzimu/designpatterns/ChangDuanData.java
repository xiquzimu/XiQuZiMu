package me.xlgp.douyinzimu.designpatterns;

import java.util.Observer;

import me.xlgp.douyinzimu.obj.changduan.ChangDuanInfo;

public class ChangDuanData extends BaseObservable<ChangDuanInfo> {
    private static ChangDuanData instance;

    private ChangDuanData() {
    }

    public static ChangDuanData getInstance() {
        if (instance == null) {
            synchronized (ChangDuanData.class) {
                if (instance == null) {
                    instance = new ChangDuanData();
                }
            }
        }
        return instance;
    }

    public void observe(Observer observer) {
        this.addObserver(observer);
    }
}
