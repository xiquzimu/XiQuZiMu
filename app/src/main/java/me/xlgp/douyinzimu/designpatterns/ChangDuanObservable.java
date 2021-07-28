package me.xlgp.douyinzimu.designpatterns;

import java.util.Observable;

import me.xlgp.douyinzimu.obj.changduan.ChangDuan;

public class ChangDuanObservable extends Observable {
    private ChangDuan changDuan;

    public ChangDuan getChangDuan() {
        return changDuan;
    }

    public void setChangDuan(ChangDuan changDuan) {
        this.changDuan = changDuan;
        setChanged();
        notifyObservers();
    }


}
