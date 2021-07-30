package me.xlgp.douyinzimu.designpatterns;

import android.view.View;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import me.xlgp.douyinzimu.obj.changduan.ChangDuan;
import me.xlgp.douyinzimu.service.PingLunService;

public class AllObserver {

    /**
     * 当前选中的唱段观察者
     */
    public static class CurrentZimuItemObserver implements Observer {
        TextView textView;

        public CurrentZimuItemObserver(View view) {
            this.textView = (TextView) view;
        }

        @Override
        public void update(Observable o, Object arg) {
            ChangDuan changDuan = ((ChangDuanObservable<ChangDuan>) o).getData();
            textView.setText(changDuan.getChangeDuanQiTa().getTitle() + " (" + changDuan.getChangeDuanQiTa().getJuMu() + ")");
        }
    }

    /**
     * 当先选中唱段观察者，
     */
    public static class ChangeCiListObserver implements Observer {

        @Override
        public void update(Observable o, Object arg) {
            ChangDuan changDuan = ((ChangDuanObservable<ChangDuan>) o).getData();
            PingLunService.getInstance().setChangeCiList(changDuan.getChangeCiList(0));
        }
    }
}
