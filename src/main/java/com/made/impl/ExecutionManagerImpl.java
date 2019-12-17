package com.made.impl;

import com.made.Context;
import com.made.ExecutionManager;
import com.made.RunnableDecorator;
import org.hsqldb.lib.Collection;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ExecutionManagerImpl implements ExecutionManager {

    @Override
    public Context execute(Runnable... tasks) {
        List<RunnableDecorator> runnableTasks = Arrays.stream(tasks)
                .map(RunnableDecorator::new)
                .collect(Collectors.toList());
        runnableTasks.forEach(RunnableDecorator::run);
        return new ContextImpl(runnableTasks);
    }
}
