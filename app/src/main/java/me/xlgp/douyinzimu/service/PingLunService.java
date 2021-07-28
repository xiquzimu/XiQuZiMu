package me.xlgp.douyinzimu.service;

import me.xlgp.douyinzimu.obj.changduan.ChangCiList;

public class PingLunService {

    private static PingLunService instance = null;
    private ChangCiList changCiList = null;

    private PingLunService() {

    }

    public static PingLunService getInstance() {
        if (instance == null) instance = new PingLunService();
        return instance;
    }

    public ChangCiList getChangeCiList() {
        return changCiList;
    }

    public void setChangeCiList(ChangCiList changCiList) {
        this.changCiList = changCiList;
    }

    public void clear() {
        changCiList = null;
    }

    public boolean hasChangeCi() {
        return changCiList != null && changCiList.hasNext();
    }
}
