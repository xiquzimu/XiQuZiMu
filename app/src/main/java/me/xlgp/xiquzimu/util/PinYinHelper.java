package me.xlgp.xiquzimu.util;

import com.github.promeg.pinyinhelper.Pinyin;

import java.util.List;

import me.xlgp.xiquzimu.model.ChangDuan;

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
