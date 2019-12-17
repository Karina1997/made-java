package com.made;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hsqldb.lib.StopWatch;

@Setter
@Getter
@RequiredArgsConstructor
public class RunnableDecorator extends Thread {
    int executionTime = -1;
    private Status status = Status.isNotStarted;
    private final Runnable task;


    public void interrupt() {
        changeStatus(Status.isInterrupted);
    }

    public synchronized Status getStatus() {
        return status;
    }

    private synchronized void changeStatus(Status newStatus) {
        this.status = newStatus;
    }

    @Override
    public void run() {
        if (getStatus() != Status.isInterrupted) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            changeStatus(Status.isRunning);
            try {
                task.run();
                stopWatch.stop();
                changeStatus(Status.isFinished);
                executionTime = (int) stopWatch.elapsedTime();
            } catch (Exception e) {
                changeStatus(Status.isFailed);
            }
        }
    }
}
