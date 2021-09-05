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

    private boolean juMu(ChangDuan changDuan, CharSequence str) {
        return Objects.requireNonNull(changDuan.getJuMu()).contains(Objects.requireNonNull(str));
    }

    private boolean name(ChangDuan changDuan, CharSequence str) {
        return Objects.requireNonNull(changDuan.getName()).contains(Objects.requireNonNull(str));
    }

    @Override
    public boolean test(ChangDuan changDuan) {
        try {
            return juMu(changDuan, liveData.getValue()) || name(changDuan, liveData.getValue());
        } catch (Exception e) {
            return false;
        }
    }
}
