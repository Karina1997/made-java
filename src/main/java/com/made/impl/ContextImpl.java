package com.made.impl;

import com.made.Context;
import com.made.ExecutionStatistics;
import com.made.Status;
import com.made.ThreadWithStatus;
import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.function.Predicate;

@AllArgsConstructor
public class ContextImpl implements Context {
    private Collection<ThreadWithStatus> tasks;

    @Override
    public int getCompletedTaskCount() {
        return getCount(x -> x.getStatus() == Status.IS_FINISHED);
    }

    @Override
    public int getFailedTaskCount() {
        return getCount(x -> x.getStatus() == Status.IS_FAILED);
    }

    @Override
    public int getInterruptedTaskCount() {
        return getCount(x -> x.getStatus() == Status.IS_INTERRUPTED);
    }


    @Override
    public void interrupt() {
        tasks.stream()
                .filter(x -> x.getStatus() == Status.IS_NOT_STARTED)
                .forEach(ThreadWithStatus::interrupt);
    }

    @Override
    public boolean isFinished() {
        return getCount(x -> x.getStatus() == Status.IS_INTERRUPTED || x.getStatus() == Status.IS_FINISHED) == tasks.size();
    }

    private int getCount(Predicate<ThreadWithStatus> filter) {
        return (int) tasks
                .stream()
                .filter(filter)
                .count();
    }

    @Override
    public void onFinish(Runnable callback) {
        Thread thread = new Thread(() -> {
            awaitTermination();
            callback.run();
        });
        thread.start();
    }

    @Override
    public ExecutionStatistics getStatistics() {
        int[] times = tasks.stream()
                .filter(x -> x.getStatus() == Status.IS_FINISHED)
                .map(ThreadWithStatus::getExecutionTime)
                .mapToInt(x -> x)
                .toArray();

        return new ExecutionStatisticsImpl(times);
    }

    @Override
    public void awaitTermination() {
        for (ThreadWithStatus x : tasks) {
            if (x.getStatus() == Status.IS_RUNNING) {
                try {
                    x.join();
                } catch (InterruptedException e) {
                    System.out.println("Already interrupted");
                }
            }
        }
    }
}
