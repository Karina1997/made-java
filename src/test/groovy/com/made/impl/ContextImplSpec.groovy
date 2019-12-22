package com.made.impl

import com.made.Status
import com.made.ThreadWithStatus
import spock.lang.Specification

class ContextImplSpec extends Specification {

    def collection

    void setup() {
        def failed = new ThreadWithStatus(Mock(Runnable))
        failed.status = Status.IS_FAILED
        def finished = new ThreadWithStatus(Mock(Runnable))
        finished.status = Status.IS_FINISHED
        def interrupted = new ThreadWithStatus(Mock(Runnable))
        interrupted.status = Status.IS_INTERRUPTED
        def notStarted = new ThreadWithStatus(Mock(Runnable))
        collection = [failed, finished, interrupted, notStarted]
    }

    def "Get all finished, failed and interrupted tasks"() {
        given:
        def context = new ContextImpl(collection)

        when:
        def completedNumber = context.getCompletedTaskCount()
        def failedNumber = context.getFailedTaskCount()
        def interruptedNumber = context.getInterruptedTaskCount()
        def isFinished = context.isFinished()

        then:
        completedNumber == 1
        failedNumber == 1
        interruptedNumber == 1
        !isFinished

    }

    def "Interrupt"() {
        given:
        def context = new ContextImpl(collection)
        def notStarted = collection[3]

        when:
        context.interrupt()

        then:
        notStarted.getStatus() == Status.IS_INTERRUPTED
    }

    def "Return finished"() {
        given:
        def finished1 = new ThreadWithStatus(Mock(Runnable))
        finished1.status = Status.IS_FINISHED
        def finished2 = new ThreadWithStatus(Mock(Runnable))
        finished2.status = Status.IS_INTERRUPTED
        def collectionFinished = [finished1, finished2]
        def context = new ContextImpl(collectionFinished)

        when:
        def result = context.isFinished()

        then:
        result
    }

    def "Get statistics"() {
        given:
        def threadDecorator2 = new ThreadWithStatus(Mock(Runnable))
        threadDecorator2.status = Status.IS_FINISHED
        threadDecorator2.executionTime = 5
        def threadDecorator3 = new ThreadWithStatus(Mock(Runnable))
        threadDecorator3.status = Status.IS_FINISHED
        threadDecorator3.executionTime = 13
        def collectionFinished = [threadDecorator2, threadDecorator3]
        def context = new ContextImpl(collectionFinished)

        when:
        def result = context.getStatistics()

        then:
        result.getAverageExecutionTimeInMs() == 9
        result.getMaxExecutionTimeInMs() == 13
        result.getMinExecutionTimeInMs() == 5
    }

}
