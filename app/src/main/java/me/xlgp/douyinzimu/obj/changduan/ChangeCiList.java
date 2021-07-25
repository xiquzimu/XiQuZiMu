package me.xlgp.douyinzimu.obj.changduan;

import java.util.ArrayList;
import java.util.ListIterator;

public class ChangeCiList extends ArrayList<ChangCi> {
    private ListIterator<ChangCi> listIterator = null;
    private ChangCi current = null;

    public ChangeCiList() {
        listIterator = this.listIterator();
    }

    public ChangCi next() {
        if (this.listIterator.hasNext()) {
            current = (ChangCi) this.listIterator.next();
        } else {
            current = null;
        }
        return current;
    }

    public boolean hasNext() {
        return listIterator.hasNext();
    }

    public ChangCi current() {
        return current;
    }

    public void setCursor(int cursor) {
        listIterator = this.listIterator(cursor);
    }
}
