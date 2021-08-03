package me.xlgp.douyinzimu.obj.changduan;

import java.util.ArrayList;
import java.util.ListIterator;

import me.xlgp.douyinzimu.designpatterns.BaseObservable;

public class ChangCiList extends ArrayList<ChangCi> {
    private ListIterator<ChangCi> listIterator = null;
    private ChangCiListObservable changCiListObservable;

    public ChangCiList() {
        listIterator = this.listIterator();
    }

    public ChangCi next() {
        if (this.listIterator.hasNext()) {
            ChangCi changCi = this.listIterator.next();
            changCiListObservable.setData(changCi);
            return changCi;
        }
        return null;
    }

    public boolean hasNext() {
        return listIterator.hasNext();
    }

    public void setChangCiListObservable(ChangCiListObservable changCiListObservable) {
        this.changCiListObservable = changCiListObservable;
    }

    public ChangCi current() {
        return this.get(listIterator.nextIndex());
    }

    public void setCursor(int cursor) {
        listIterator = this.listIterator(cursor);
    }

    public static class ChangCiListObservable extends BaseObservable<ChangCi> {
    }
}
