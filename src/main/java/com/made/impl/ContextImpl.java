package com.made.impl;

import com.made.Context;
import com.made.ExecutionStatistics;
import com.made.ThreadDecorator;
import com.made.Status;
import lombok.AllArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
public class ContextImpl implements Context {
    private Collection<ThreadDecorator> tasks;

    @Override
    public int getCompletedTaskCount() {
        return (int) tasks.stream()
                .filter(x -> x.getStatus() == Status.IS_FINISHED)
                .count();
    }

    @Override
    public int getFailedTaskCount() {
        return (int) tasks.stream()
                .filter(x -> x.getStatus() == Status.IS_FAILED)
                .count();
    }

    @Override
    public int getInterruptedTaskCount() {
        return (int) tasks
                .stream().filter(x -> x.getStatus() == Status.IS_INTERRUPTED)
                .count();
    }

    @Override
    public void interrupt() {
        tasks.stream()
                .filter(x -> x.getStatus() == Status.IS_NOT_STARTED)
                .forEach(ThreadDecorator::interrupt);
    }

    @Override
    public boolean isFinished() {
        return (int) tasks.stream()
                .filter(x -> x.getStatus() == Status.IS_INTERRUPTED || x.getStatus() == Status.IS_FINISHED)
                .count() == tasks.size();
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
                .map(ThreadDecorator::getExecutionTime)
                .mapToInt(x -> x)
                .toArray();

        return new ExecutionStatisticsImpl(times);
    }

    @Override
    public void awaitTermination() {
        for (ThreadDecorator x : tasks) {
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
