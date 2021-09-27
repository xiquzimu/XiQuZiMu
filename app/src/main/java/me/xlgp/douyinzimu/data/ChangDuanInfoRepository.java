package me.xlgp.douyinzimu.data;

import java.util.List;
import java.util.NoSuchElementException;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import me.xlgp.douyinzimu.designpatterns.ObserverHelper;
import me.xlgp.douyinzimu.model.ChangCi;
import me.xlgp.douyinzimu.model.ChangDuan;
import me.xlgp.douyinzimu.model.ChangDuanInfo;
import me.xlgp.douyinzimu.util.ChangDuanHelper;

public class ChangDuanInfoRepository {

    public Observable<ChangDuanInfo> getChangDuanInfo(Integer changDuanId) {
        ChangCiRepository changCiRepository = new ChangCiRepository();
        ChangDuanRepository changDuanRepository = new ChangDuanRepository();
        return Observable.create((ObservableOnSubscribe<ChangDuanInfo>) emitter -> {
            ChangDuanInfo changDuanInfo = new ChangDuanInfo();

            ChangDuan changDuan = changDuanRepository.get(changDuanId);
            if (changDuan == null) {
                throw new NoSuchElementException("无法查询唱段");
            }

            List<ChangCi> changCiList = changCiRepository.listByChangDuanId(changDuanId);
            if (changCiList == null || changCiList.isEmpty()) {
                throw new NoSuchElementException("无法查询唱词");
            }

            changDuanInfo.setChangDuan(changDuan);
            changDuanInfo.setChangCiList(ChangDuanHelper.parseChangCiList(changDuan, changCiList));
            emitter.onNext(changDuanInfo);
        }).compose(ObserverHelper.transformer());
    }
}
