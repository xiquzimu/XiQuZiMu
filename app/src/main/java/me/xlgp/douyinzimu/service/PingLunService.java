package me.xlgp.douyinzimu.service;

import me.xlgp.douyinzimu.obj.changduan.ChangeCiList;

public class PingLunService {

    private ChangeCiList changeCiList = null;
    private static PingLunService instance = null;

    private PingLunService() {

    }

    public static PingLunService getInstance() {
        if (instance == null) instance = new PingLunService();
        return instance;
    }

    public ChangeCiList getChangeCiList() {
        return changeCiList;
    }

    public void setChangeCiList(ChangeCiList changeCiList) {
        this.changeCiList = changeCiList;
    }

    public void clear() {
        changeCiList = null;
    }

    public boolean hasChangeCi(){
        return !(changeCiList == null);
    }
}
