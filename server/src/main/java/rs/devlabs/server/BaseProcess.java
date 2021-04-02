package rs.devlabs.server;

public abstract class BaseProcess extends Thread implements Thread.UncaughtExceptionHandler {

    public BaseProcess(String name) {
        super(name);
        setUncaughtExceptionHandler(this);
    }

    public abstract void stopThread();
}
