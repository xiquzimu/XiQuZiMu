package me.xlgp.douyinzimu.util;

import com.github.promeg.pinyinhelper.Pinyin;

import java.util.List;

import me.xlgp.douyinzimu.model.ChangDuan;

public class PinYinHelper {

    public static void sortByPinYin(List<ChangDuan> list) {
        list.sort((o1, o2) -> {
            try {
                return Pinyin.toPinyin(o1.getJuMu(), "").compareTo(Pinyin.toPinyin(o2.getJuMu(), ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        });
    }
}
