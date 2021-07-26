package me.xlgp.douyinzimu.util;

import java.util.ArrayList;
import java.util.List;

import me.xlgp.douyinzimu.obj.changduan.ChangCiList;
import me.xlgp.douyinzimu.obj.changduan.ChangDuan;
import me.xlgp.douyinzimu.zimu.NvfumaChangDuan;
import me.xlgp.douyinzimu.zimu.TianXianPeiChangDuan;

public class ChangDuanHelper {
    public static List<ChangDuan> getChangDuanList(){
        ChangDuan changDuan = new ChangDuan();
        List<ChangDuan> list = new ArrayList<>();
        changDuan.setChangeCiList(new ChangCiList());
        list.add(new NvfumaChangDuan().getZhongZhuangYuan());
        list.add(new TianXianPeiChangDuan().getFuQiHuanJia());
        list.add(new NvfumaChangDuan().getDongFang());
        return list;
    }
}
