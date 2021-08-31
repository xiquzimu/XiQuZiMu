package me.xlgp.douyinzimu.predicate;

import androidx.lifecycle.MutableLiveData;

import java.util.Objects;
import java.util.function.Predicate;

import me.xlgp.douyinzimu.model.ChangDuan;

public class ChangDuanPredicate implements Predicate<ChangDuan> {
    private final MutableLiveData<CharSequence> liveData;

    public ChangDuanPredicate(MutableLiveData<CharSequence> liveData) {
        this.liveData = liveData;
    }

    @Override
    public boolean test(ChangDuan changDuan) {
        try {
            return Objects.requireNonNull(changDuan.getName())
                    .contains(Objects.requireNonNull(liveData.getValue()));
        } catch (Exception e) {
            return false;
        }
    }
}
