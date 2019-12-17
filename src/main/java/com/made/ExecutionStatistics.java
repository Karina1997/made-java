package com.made;

public interface ExecutionStatistics {
    int getMinExecutionTimeInMs(); // минимальное время выполнения среди тасков в миллисекундах

    int getMaxExecutionTimeInMs(); // максимальное время выполнения среди тасков в миллисекундах

    int getAverageExecutionTimeInMs(); //среднее арифметическое время выполнения тасков в миллисекундах.

}
