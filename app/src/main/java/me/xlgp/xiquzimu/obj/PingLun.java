package me.xlgp.xiquzimu.obj;

public class PingLun {

    private static PingLun instance = null;
    private final int STARTED = 1;
    private final int STOPPED = 0;
    private int STATUS;

    private PingLun() {
        STATUS = STOPPED;
    }

    public static PingLun getInstance() {
        if (instance == null) {
            instance = new PingLun();
        }
        return instance;
    }

    public void change(boolean checked) {
        if (checked) {
            instance.start();
        } else {
            instance.stop();
        }
    }

    public void start() {
        STATUS = STARTED;
    }

    public boolean disabled() {
        return STATUS == STOPPED;
    }

    public void stop() {
        STATUS = STOPPED;
    }
}
