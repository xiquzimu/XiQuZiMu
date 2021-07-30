package me.xlgp.douyinzimu.designpatterns;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import me.xlgp.douyinzimu.obj.changduan.ChangDuan;
import me.xlgp.douyinzimu.service.PingLunService;

public class AllObserver {

    private static ChangDuan getChangDuan(Observable o) {

        ChangDuanObservable observable = new ChangDuanObservable<>();
        if (o instanceof ChangDuanObservable) {
            observable = ChangDuanObservable.class.cast(o);
        }
        if (observable.getData() instanceof ChangDuan){
            return ChangDuan.class.cast(observable.getData());
        }
        return null;
    }

    /**
     * 当前选中的唱段观察者
     */
    public static class CurrentZimuItemObserver implements Observer {
        TextView textView;

        public CurrentZimuItemObserver(View view) {
            this.textView = (TextView) view;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void update(Observable o, Object arg) {
            ChangDuan changDuan = getChangDuan(o);
            textView.setText(changDuan.getChangeDuanQiTa().getTitle() + " (" + changDuan.getChangeDuanQiTa().getJuMu() + ")");
        }
    }

    /**
     * 当先选中唱段观察者，
     */
    public static class ChangeCiListObserver implements Observer {

        @Override
        public void update(Observable o, Object arg) {
            ChangDuan changDuan = getChangDuan(o);
            PingLunService.getInstance().setChangeCiList(changDuan.getChangeCiList(0));
        }
    }
}
