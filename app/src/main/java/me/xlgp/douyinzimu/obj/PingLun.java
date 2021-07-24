package me.xlgp.douyinzimu.obj;

public class PingLun {

    private final int STARTED = 1;
    private final int RUNNING = 1;
    private final int STOPPED = 0;
    private int STATUS;

    private static PingLun instance = null;

    private PingLun() {
        STATUS = STOPPED;
    }

    public static PingLun getInstance() {
        if (instance == null) {
            instance = new PingLun();
        }
        return instance;
    }

    public void start() {
       STATUS = STARTED;
    }
    public boolean disabled(){
        return STATUS == STOPPED;
    }
    public void stop() {
        STATUS = STOPPED;
    }

    public void change() {
        STATUS = STATUS == STOPPED ? STARTED : STOPPED;
    }

    public boolean isRun() {
        return STATUS == RUNNING;
    }
}
