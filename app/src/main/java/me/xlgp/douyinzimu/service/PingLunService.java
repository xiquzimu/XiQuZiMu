package me.xlgp.douyinzimu.service;

import me.xlgp.douyinzimu.obj.ZiMuList;

public class PingLunService {

    private ZiMuList ziMuList = null;
    private static PingLunService instance = null;

    private PingLunService() {

    }

    public boolean running() {
        return true;
    }

    public static PingLunService getInstance() {
        if (instance == null) instance = new PingLunService();
        return instance;
    }

    public void setZiMuList(ZiMuList ziMuList) {
        this.ziMuList = ziMuList;
    }

    public ZiMuList getZiMuList() {
        return ziMuList;
    }

    public void clear() {
        ziMuList = null;
    }
}
