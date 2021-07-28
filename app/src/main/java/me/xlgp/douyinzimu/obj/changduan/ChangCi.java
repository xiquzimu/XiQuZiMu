package me.xlgp.douyinzimu.obj.changduan;

public class ChangCi {
    private String mContent = null;
    private String mTime = null;
    private long delayMillis = Long.MAX_VALUE;

    public ChangCi(String mContent, String mTime) {
        this.mContent = mContent;
        this.mTime = mTime;
        delayMillis = 2000;
    }

    public ChangCi(String mContent, String mTime, long delayMillis) {
        this.mContent = mContent;
        this.mTime = mTime;
        this.delayMillis = delayMillis;
    }

    public String getContent() {
        return mContent;
    }

    public String getTime() {
        return mTime;
    }

    public long getDelayMillis() {
        return delayMillis;
    }

    public void setDelayMillis(long delayMillis) {
        this.delayMillis = delayMillis;
    }

    @Override
    public String toString() {
        return "ChangCi{" +
                "mContent='" + mContent + '\'' +
                ", mTime='" + mTime + '\'' +
                ", delayMillis=" + delayMillis +
                '}';
    }
}
