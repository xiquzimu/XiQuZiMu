package me.xlgp.douyinzimu.zimu;

import me.xlgp.douyinzimu.obj.changduan.ChangCi;
import me.xlgp.douyinzimu.obj.changduan.ChangDuan;

public class TianXianPeiChangDuan extends ChangDuan {

    public TianXianPeiChangDuan(){
        this.getChangeDuanQiTa().setJuMu("天仙配");
    }
    /**
     * @return
     */
    public TianXianPeiChangDuan  getFuQiHuanJia(){

        this.getChangeDuanQiTa().setTitle("夫妻双双把家还");

        this.getChangeCiList().add(new ChangCi("请欣赏黄梅戏唱段：树上鸟儿成双对", "00:00",25*1000));
        this.getChangeCiList().add(new ChangCi("女：树上的鸟儿成双对", "00:27",6*1000));
        this.getChangeCiList().add(new ChangCi("男：绿水青山带笑颜", "00:34",7*1000));
        this.getChangeCiList().add(new ChangCi("女：随手摘下花一朵", "00:42",4*1000));
        this.getChangeCiList().add(new ChangCi("男：我于娘子戴发间", "00:47",7*1000));
        this.getChangeCiList().add(new ChangCi("女：从今不再受那奴役苦", "00:55",5*1000));
        this.getChangeCiList().add(new ChangCi("男：夫妻双双把家还", "01:01",7*1000));
        this.getChangeCiList().add(new ChangCi("女：你耕田来我织布", "01:08",7*1000));
        this.getChangeCiList().add(new ChangCi("男：我挑水来你浇园", "01:16",6*1000));
        this.getChangeCiList().add(new ChangCi("女：寒窑虽破能避风雨", "01:23",6*1000));
        this.getChangeCiList().add(new ChangCi("男：夫妻恩爱苦也甜", "01:30",6*1000));
        this.getChangeCiList().add(new ChangCi("合：你我好比鸳鸯鸟", "01:37",6*1000));
        this.getChangeCiList().add(new ChangCi("合：比翼双飞在人间", "01:43",2*1000));
        this.getChangeCiList().add(new ChangCi("本唱段：树上鸟儿成双对，出自黄梅戏《天仙配》选段", "01:43",2*1000));
        this.getChangeCiList().setCursor(0);
        return this;
    }
}
