package me.xlgp.douyinzimu.obj.changduan;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Observable;
import java.util.Observer;

import me.xlgp.douyinzimu.model.ChangCi;

public class ChangCiList extends ArrayList<ChangCi> {
    private ListIterator<ChangCi> listIterator;
    private ChangCiObservable changCiObservable;

    public ChangCiList() {
        changCiObservable = new ChangCiObservable();
        listIterator = this.listIterator();
    }

    public ChangCi next() {
        if (this.listIterator.hasNext()) {
            ChangCi changCi = this.listIterator.next();
            changCiObservable.notifyObservers(changCi);
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

    public void setCursor(int cursor) {
        listIterator = this.listIterator(cursor);
    }

    public void observe(Observer observer) {
        changCiObservable.addObserver(observer);
    }

    public class ChangCiObservable extends Observable {

    }
}
