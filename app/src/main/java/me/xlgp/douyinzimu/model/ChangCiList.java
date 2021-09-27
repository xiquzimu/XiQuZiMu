package me.xlgp.douyinzimu.model;

import java.util.ArrayList;
import java.util.ListIterator;

public class ChangCiList extends ArrayList<ChangCi> {
    private ListIterator<ChangCi> listIterator;

    public ChangCiList() {
        listIterator = this.listIterator();
    }

    public ChangCi next() {
        if (this.listIterator.hasNext()) {
            return this.listIterator.next();
        }
        return null;
    }

    public boolean hasNext() {
        return listIterator.hasNext();
    }

    public ChangCi current() {
        return this.get(listIterator.nextIndex());
    }

    public int currentIndex() {
        return listIterator.nextIndex();
    }

    public void setCursor(int cursor) {
        listIterator = this.listIterator(cursor);
    }
}
