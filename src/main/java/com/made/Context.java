package com.made;

public interface Context {
    int getCompletedTaskCount();

    int getFailedTaskCount();

    int getInterruptedTaskCount();

    void interrupt();

    boolean isFinished();

    void onFinish(Runnable callback);

    ExecutionStatistics getStatistics();

    void awaitTermination();
}
