package me.xlgp.douyinzimu.obj;

/**
 * 点赞对象
 */
public class DianZan {
    //点赞数量
    private int count;
    private int initCount = 100;

    public DianZan() {
        count = initCount;
    }

    public DianZan(int count) {
        this.count = count;
        initCount = count;
    }

    public void init() {
        count = initCount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isEmpty() {
        return this.count == 0;
    }

    public void updateCount() {
        if (this.count <= 0) {
            this.count = 0;
            return;
        }
        count--;
    }
}
