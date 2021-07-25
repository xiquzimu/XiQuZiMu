package me.xlgp.douyinzimu.zimu;

import me.xlgp.douyinzimu.obj.changduan.ChangCi;
import me.xlgp.douyinzimu.obj.changduan.ChangeDuan;

/**
 * 女驸马
 */
public class NvfumaChangDuan extends ChangeDuan {

    public NvfumaChangDuan(){
        this.getChangeDuanQiTa().setJuMu("女驸马");
    }
    public NvfumaChangDuan getZhongZhuangYuan() {

        this.getChangeDuanQiTa().setTitle("为救李郎离家园");

        this.getChangeCiList().add(new ChangCi("请欣赏黄梅戏唱段：谁料皇榜中状元", "00:00", 16*1000));
        this.getChangeCiList().add(new ChangCi("为救李郎离家园", "00:18", 4 * 1000));
        this.getChangeCiList().add(new ChangCi("谁料皇榜中状元", "00:23", 8 * 1000));
        this.getChangeCiList().add(new ChangCi("中状元着红袍", "00:34", 1 * 1000));
        this.getChangeCiList().add(new ChangCi("帽插宫花好哇好新鲜哪", "00:36", 15 * 1000));
        this.getChangeCiList().add(new ChangCi("我也曾赴过琼林宴", "00:53", 4 * 1000));
        this.getChangeCiList().add(new ChangCi("我也曾打马御街前", "00:58", 10 * 1000));
        this.getChangeCiList().add(new ChangCi("人人夸我潘安貌", "01:09", 2 * 1000));
        this.getChangeCiList().add(new ChangCi("原来纱帽照哇照婵娟哪", "01:12", 6 * 1000));
        this.getChangeCiList().add(new ChangCi("我考状元不为把名显", "01:19", 5 * 1000));
        this.getChangeCiList().add(new ChangCi("我考状元不为做高官", "00:25", 4 * 1000));
        this.getChangeCiList().add(new ChangCi("为了多情的李公子", "00:29", 3 * 1000));
        this.getChangeCiList().add(new ChangCi("夫妻恩爱花儿好月儿圆哪", "00:33", 0));
        this.getChangeCiList().add(new ChangCi("本唱段：谁料皇榜中状元，出自黄梅戏《女驸马》选段", "00:33", 0));
        this.getChangeCiList().setCursor(0);

        return this;
    }
}
