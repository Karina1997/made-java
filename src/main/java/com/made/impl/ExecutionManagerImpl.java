package com.made.impl;

import com.made.Context;
import com.made.ExecutionManager;
import com.made.ThreadWithStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ExecutionManagerImpl implements ExecutionManager {

    @Override
    public Context execute(Runnable... tasks) {
        List<ThreadWithStatus> runnableTasks = Arrays.stream(tasks)
                .map(ThreadWithStatus::new)
                .collect(Collectors.toList());
        runnableTasks.forEach(ThreadWithStatus::start);
        return new ContextImpl(runnableTasks);
    }
}
