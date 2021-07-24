package me.xlgp.douyinzimu.zimu;

import me.xlgp.douyinzimu.obj.ZiMu;
import me.xlgp.douyinzimu.obj.ZiMuList;

/**
 * 女驸马
 */
public class NvfumaZimu {

    public static ZiMuList getZhongZhuangYuan() {
        ZiMuList ziMuList = new ZiMuList();
        ziMuList.add(new ZiMu("中状元，出自黄梅戏《女驸马》", "00:00", 18*1000));
        ziMuList.add(new ZiMu("为救李郎离家园", "00:18", 5 * 1000));
        ziMuList.add(new ZiMu("谁料皇榜中状元", "00:23", 9 * 1000));
        ziMuList.add(new ZiMu("中状元着红袍", "00:34", 2 * 1000));
        ziMuList.add(new ZiMu("帽插宫花好哇好新鲜哪", "00:36", 17 * 1000));
        ziMuList.add(new ZiMu("我也曾赴过琼林宴", "00:53", 5 * 1000));
        ziMuList.add(new ZiMu("我也曾打马御街前", "00:58", 11 * 1000));
        ziMuList.add(new ZiMu("人人夸我潘安貌", "01:09", 3 * 1000));
        ziMuList.add(new ZiMu("原来纱帽照哇照婵娟哪", "01:12", 7 * 1000));
        ziMuList.add(new ZiMu("我考状元不为把名显", "01:19", 6 * 1000));
        ziMuList.add(new ZiMu("我考状元不为做高官", "00:25", 4 * 1000));
        ziMuList.add(new ZiMu("为了多情的李公子", "00:29", 4 * 1000));
        ziMuList.add(new ZiMu("夫妻恩爱花儿好月儿圆哪", "00:33", 0));
        ziMuList.setCursor(0);
        return ziMuList;
    }
}
