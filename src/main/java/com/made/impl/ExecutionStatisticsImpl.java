package com.made.impl;

import com.made.ExecutionStatistics;
import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public class ExecutionStatisticsImpl implements ExecutionStatistics {
    private final int[] times;

    @Override
    public int getMinExecutionTimeInMs() {
        return Arrays.stream(times)
                .min()
                .orElse(-1);
    }

    @Override
    public int getMaxExecutionTimeInMs() {
        return Arrays.stream(times)
                .max()
                .orElse(-1);
    }

    @Override
    public int getAverageExecutionTimeInMs() {
        return (int) Arrays.stream(times)
                .average()
                .orElse(-1);
    }
}
