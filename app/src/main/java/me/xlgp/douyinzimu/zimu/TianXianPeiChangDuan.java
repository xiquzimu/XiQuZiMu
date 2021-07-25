package me.xlgp.douyinzimu.zimu;

import me.xlgp.douyinzimu.obj.changduan.ChangCi;
import me.xlgp.douyinzimu.obj.changduan.ChangeDuan;

public class TianXianPeiChangDuan extends ChangeDuan {

    public TianXianPeiChangDuan(){
        this.getChangeDuanQiTa().setJuMu("天仙配");
    }
    /**
     * @return
     */
    public TianXianPeiChangDuan  getFuQiHuanJia(){

        this.getChangeDuanQiTa().setTitle("夫妻双双把家还");

        this.getChangeCiList().add(new ChangCi("请欣赏黄梅戏唱段：树上鸟儿成双对", "00:00",25*1000));
        this.getChangeCiList().add(new ChangCi("女：树上的鸟儿成双对", "00:25",9*1000));
        this.getChangeCiList().add(new ChangCi("男：绿水青山带笑颜", "00:38",13*1000));
        this.getChangeCiList().add(new ChangCi("女：随手摘下花一朵", "00:45",7*1000));
        this.getChangeCiList().add(new ChangCi("男：我于娘子戴发间", "00:47",2*1000));
        this.getChangeCiList().add(new ChangCi("女：从今不再受那奴役苦", "00:58",11*1000));
        this.getChangeCiList().setCursor(0);
        return this;
    }
}
