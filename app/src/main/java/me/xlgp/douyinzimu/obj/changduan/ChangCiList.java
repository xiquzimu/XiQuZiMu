package me.xlgp.douyinzimu.obj.changduan;

import java.util.ArrayList;
import java.util.ListIterator;

public class ChangCiList extends ArrayList<ChangCi> {
    private ListIterator<ChangCi> listIterator = null;

    public ChangCiList() {
        listIterator = this.listIterator();
    }

    public ChangCi next() {
        if (this.listIterator.hasNext()) {
            return (ChangCi) this.listIterator.next();
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
}
