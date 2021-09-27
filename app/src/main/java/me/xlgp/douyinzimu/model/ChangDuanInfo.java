package me.xlgp.douyinzimu.model;


/**
 * 唱词：内容，时间，时间间隔，
 * 唱段
 * 额外信息：名称，剧目，
 * 唱词列表，
 */
public class ChangDuanInfo {
    private ChangDuan changDuan;
    private ChangCiList changCiList;

    public ChangDuanInfo() {
        changCiList = new ChangCiList();
        changDuan = new ChangDuan();
    }

    public ChangDuan getChangDuan() {
        return changDuan;
    }

    public void setChangDuan(ChangDuan changDuan) {
        this.changDuan = changDuan;
    }

    public void setChangCiList(ChangCiList changCiList) {
        this.changCiList = changCiList;
    }

    public ChangCiList getChangeCiList() {
        return changCiList;
    }

    public ChangCiList getChangeCiList(int cursor) {
        changCiList.setCursor(cursor);
        return changCiList;
    }
}
