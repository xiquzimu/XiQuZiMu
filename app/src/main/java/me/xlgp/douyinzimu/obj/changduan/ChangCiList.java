package me.xlgp.douyinzimu.obj.changduan;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Observer;

import me.xlgp.douyinzimu.designpatterns.BaseObservable;
import me.xlgp.douyinzimu.model.ChangCi;

public class ChangCiList extends ArrayList<ChangCi> {
    private final ChangCiObservable changCiObservable;
    private ListIterator<ChangCi> listIterator;

    public ChangCiList() {
        changCiObservable = new ChangCiObservable();
        listIterator = this.listIterator();
    }

    public ChangCi next() {
        if (this.listIterator.hasNext()) {
            ChangCi changCi = this.listIterator.next();
            changCiObservable.setData(changCi);
            return changCi;
        }
        return null;
    }

    public boolean hasNext() {
        return listIterator.hasNext();
    }

    public ChangCi current() {
        return this.get(listIterator.nextIndex());
    }

    public int currentIndex(){
        return listIterator.nextIndex();
    }

    public void setCursor(int cursor) {
        listIterator = this.listIterator(cursor);
    }

    public void observe(Observer observer) {
        changCiObservable.addObserver(observer);
    }

    public static class ChangCiObservable extends BaseObservable<ChangCi> {

    }
}
