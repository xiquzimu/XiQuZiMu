package me.xlgp.xiquzimu.designpatterns;

import java.util.Observable;

public abstract class BaseObservable<T> extends Observable {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
        changeData();
    }

    protected void changeData() {
        setChanged();
        notifyObservers(data);
    }
}
