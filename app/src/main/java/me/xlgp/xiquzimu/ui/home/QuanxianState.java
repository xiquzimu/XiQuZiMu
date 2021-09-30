package me.xlgp.xiquzimu.ui.home;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;

import me.xlgp.xiquzimu.R;

class FloatQuanxianState extends Result<QuanxianState> {

    FloatQuanxianState(QuanxianState data) {
        super(data);
    }

    static class Succes extends Result.Succes<QuanxianState> {
        public Succes() {
            super(new QuanxianState(R.color.white, R.string.openFloatingText));
        }
    }

    static class Error extends Result.Error<QuanxianState> {
        public Error() {
            super(new QuanxianState(R.color.red, R.string.no_openFloatingText));
        }
    }
}

class AccessibilitySettingStatus extends Result<QuanxianState> {
    public AccessibilitySettingStatus(QuanxianState quanxianState) {
        super(quanxianState);
    }

    static class Succes extends Result.Succes<QuanxianState> {
        public Succes() {
            super(new QuanxianState(R.color.white, R.string.ApenaccesibilityText));
        }
    }

    static class Error extends Result.Error<QuanxianState> {
        public Error() {
            super(new QuanxianState(R.color.red, R.string.NoApenaccesibilityText));
        }
    }
}

public class QuanxianState {

    private @ColorRes
    int resouceId;

    private @StringRes
    int textid;

    public QuanxianState() {

    }

    public QuanxianState(@ColorRes int resouceId, @StringRes int textid) {
        this.resouceId = resouceId;
        this.textid = textid;
    }

    public int getResouceId() {
        return resouceId;
    }

    public void setResouceId(int resouceId) {
        this.resouceId = resouceId;
    }

    public int getTextid() {
        return textid;
    }

    public void setTextid(int textid) {
        this.textid = textid;
    }
}
