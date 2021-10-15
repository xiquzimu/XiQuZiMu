package me.xlgp.xiquzimu.predicate;

import androidx.lifecycle.MutableLiveData;

import java.util.Objects;
import java.util.function.Predicate;

import me.xlgp.xiquzimu.model.ChangDuan;
import me.xlgp.xiquzimu.params.TagChangDuan;

public class ChangDuanPredicate implements Predicate<TagChangDuan> {
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
    public boolean test(TagChangDuan changDuan) {
        try {
            return juMu(changDuan, liveData.getValue()) || name(changDuan, liveData.getValue());
        } catch (Exception e) {
            return false;
        }
    }
}
