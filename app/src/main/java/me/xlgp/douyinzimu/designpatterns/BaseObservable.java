package me.xlgp.douyinzimu.designpatterns;

import java.util.Observable;

public class BaseObservable<T> extends Observable {
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
