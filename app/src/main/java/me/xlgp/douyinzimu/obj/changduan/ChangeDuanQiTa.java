package me.xlgp.douyinzimu.obj.changduan;

//唱段其他信息
public class ChangeDuanQiTa {
    private String title;//唱段名称
    private String juMu; //剧目
    private long offset; //时间间隔
    private String juZhong; //剧种

    public ChangeDuanQiTa() {
        this.offset = 0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJuMu() {
        return juMu;
    }

    public void setJuMu(String juMu) {
        this.juMu = juMu;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public String getJuZhong() {
        return juZhong;
    }

    public void setJuZhong(String juZhong) {
        this.juZhong = juZhong;
    }
}
