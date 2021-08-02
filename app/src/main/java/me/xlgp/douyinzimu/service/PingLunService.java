package me.xlgp.douyinzimu.service;

import me.xlgp.douyinzimu.obj.changduan.ChangDuan;

public class PingLunService {

    private static PingLunService instance = null;
    private ChangDuan changDuan = null;

    private PingLunService() {

    }

    public static PingLunService getInstance() {
        if (instance == null) instance = new PingLunService();
        return instance;
    }

    public ChangDuan getChangDuan() {
        return changDuan;
    }

    public void setChangDuan(ChangDuan changDuan) {
        this.changDuan = changDuan;
    }

    public void clear() {
        changDuan = null;
    }

    public boolean hasChangeCi() {
        return changDuan != null && changDuan.getChangeCiList().hasNext();
    }
}
