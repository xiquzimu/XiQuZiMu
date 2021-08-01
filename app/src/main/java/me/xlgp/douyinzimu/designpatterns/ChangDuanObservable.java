package me.xlgp.douyinzimu.designpatterns;

import java.util.Observable;

public class ChangDuanObservable<T> extends Observable {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        setData(data, null);
    }

    public void setData(T data, Object arg) {
        this.data = data;
        changeData(arg);
    }

    protected void changeData(Object arg) {
        setChanged();
        notifyObservers(arg);
    }
}
