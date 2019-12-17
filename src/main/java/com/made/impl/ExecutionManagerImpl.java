package com.made.impl;

import com.made.Context;
import com.made.ExecutionManager;
import com.made.ThreadDecorator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ExecutionManagerImpl implements ExecutionManager {

    @Override
    public Context execute(Runnable... tasks) {
        List<ThreadDecorator> runnableTasks = Arrays.stream(tasks)
                .map(ThreadDecorator::new)
                .collect(Collectors.toList());
        runnableTasks.forEach(ThreadDecorator::start);
        return new ContextImpl(runnableTasks);
    }
}
