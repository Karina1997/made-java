package com.made;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hsqldb.lib.StopWatch;

@Getter
@RequiredArgsConstructor
public class ThreadWithStatus extends Thread {
    private int executionTime;
    private Status status = Status.IS_NOT_STARTED;
    private final Runnable task;


    public void interrupt() {
        setStatus(Status.IS_INTERRUPTED);
    }

    public synchronized Status getStatus() {
        return status;
    }

    public synchronized void setStatus(Status newStatus) {
        this.status = newStatus;
    }

    @Override
    public void run() {
        if (getStatus() != Status.IS_INTERRUPTED) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            setStatus(Status.IS_RUNNING);
            try {
                task.run();
                setStatus(Status.IS_FINISHED);
                executionTime = (int) stopWatch.elapsedTime();
            } catch (Exception e) {
                setStatus(Status.IS_FAILED);
            } finally {
                stopWatch.stop();
            }
        }
    }
}
