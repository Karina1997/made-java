package com.made;

public interface ExecutionManager {
    Context execute(Runnable... tasks);
}
