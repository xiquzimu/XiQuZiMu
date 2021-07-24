package me.xlgp.douyinzimu.obj;

import java.util.ArrayList;
import java.util.ListIterator;

public class ZiMuList extends ArrayList<ZiMu> {
    private ListIterator<ZiMu> listIterator = null;
    private ZiMu current = null;

    public ZiMuList() {
        listIterator = this.listIterator();
    }

    public ZiMu next() {
        if (this.listIterator.hasNext()) {
            current = (ZiMu) this.listIterator.next();
        } else {
            current = null;
        }
        return current;
    }

    public boolean hasNext() {
        return listIterator.hasNext();
    }

    public ZiMu current() {
        return current;
    }

    public void setCursor(int cursor) {
        listIterator = this.listIterator(cursor);
    }
}
