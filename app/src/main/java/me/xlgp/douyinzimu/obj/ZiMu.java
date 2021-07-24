package me.xlgp.douyinzimu.obj;

/**
 * 字幕
 */
public class ZiMu {
    private String mContent = null;
    private String mTime = null;
    private long delayMillis = Long.MAX_VALUE;

    public ZiMu(String mContent, String mTime){
        this.mContent = mContent;
        this.mTime = mTime;
        delayMillis = 2000;
    }

    public ZiMu(String mContent, String mTime, long delayMillis){
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

    public long getDelayMillis(){
        return delayMillis;
    }

    @Override
    public String toString() {
        return "ZiMu{" +
                "mContent='" + mContent + '\'' +
                ", mTime='" + mTime + '\'' +
                ", delayMillis=" + delayMillis +
                '}';
    }
}
