package com.made.impl;

import com.made.Context;
import com.made.ExecutionStatistics;
import com.made.RunnableDecorator;
import com.made.Status;
import lombok.AllArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
public class ContextImpl implements Context {
    private Collection<RunnableDecorator> tasks;

    @Override
    public int getCompletedTaskCount() {
        return (int) tasks.stream()
                .filter(x -> x.getStatus() == Status.isFinished)
                .count();
    }

    @Override
    public int getFailedTaskCount() {
        return (int) tasks.stream()
                .filter(x -> x.getStatus() == Status.isFailed)
                .count();
    }

    @Override
    public int getInterruptedTaskCount() {
        return (int) tasks
                .stream().filter(x -> x.getStatus() == Status.isInterrupted)
                .count();
    }

    @Override
    public void interrupt() {
        tasks.stream()
                .filter(x -> x.getStatus() == Status.isNotStarted)
                .forEach(RunnableDecorator::interrupt);
    }

    @Override
    public boolean isFinished() {
        return (int) tasks.stream()
                .filter(x -> x.getStatus() == Status.isInterrupted || x.getStatus() == Status.isFinished)
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
                .filter(x -> x.getStatus() == Status.isFinished)
                .map(RunnableDecorator::getExecutionTime)
                .mapToInt(x -> x)
                .toArray();

        return new ExecutionStatisticsImpl(times);
    }

    @Override
    public void awaitTermination() {
        for (RunnableDecorator x : tasks) {
            if (x.getStatus() == Status.isRunning) {
                try {
                    x.join();
                } catch (InterruptedException e) {
                    System.out.println("Already interrupted");
                }
            }
        }
    }
}
