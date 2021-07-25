package me.xlgp.douyinzimu.obj.changduan;


/**
 * 唱词：内容，时间，时间间隔，
 * 唱段
 * 额外信息：名称，剧目，
 * 唱词列表，
 */
public class ChangeDuan{
    private ChangeDuanQiTa changeDuanQiTa;
    private ChangeCiList changeCiList;

    public ChangeDuan(){
        changeCiList = new ChangeCiList();
        changeDuanQiTa = new ChangeDuanQiTa();
    }

    public ChangeDuanQiTa getChangeDuanQiTa() {
        return changeDuanQiTa;
    }

    public ChangeCiList getChangeCiList() {
        return changeCiList;
    }

    public void setChangeDuanQiTa(ChangeDuanQiTa changeDuanQiTa) {
        this.changeDuanQiTa = changeDuanQiTa;
    }

    public void setChangeCiList(ChangeCiList changeCiList) {
        this.changeCiList = changeCiList;
    }
}
