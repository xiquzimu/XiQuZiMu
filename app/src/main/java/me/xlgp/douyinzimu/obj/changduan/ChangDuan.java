package me.xlgp.douyinzimu.obj.changduan;


/**
 * 唱词：内容，时间，时间间隔，
 * 唱段
 * 额外信息：名称，剧目，
 * 唱词列表，
 */
public class ChangDuan {
    private ChangeDuanQiTa changeDuanQiTa;
    private ChangCiList changCiList;

    public ChangDuan(){
        changCiList = new ChangCiList();
        changeDuanQiTa = new ChangeDuanQiTa();
    }

    public ChangeDuanQiTa getChangeDuanQiTa() {
        return changeDuanQiTa;
    }

    public ChangCiList getChangeCiList() {
        return changCiList;
    }

    public ChangCiList getChangeCiList(int cursor){
        changCiList.setCursor(cursor);
        return changCiList;
    }

    public void setChangeDuanQiTa(ChangeDuanQiTa changeDuanQiTa) {
        this.changeDuanQiTa = changeDuanQiTa;
    }

    public void setChangeCiList(ChangCiList changCiList) {
        this.changCiList = changCiList;
    }
}
