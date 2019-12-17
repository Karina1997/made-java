package com.made.impl

import com.made.Status
import com.made.ThreadDecorator
import spock.lang.Specification

class ContextImplSpec extends Specification {

    def collection

    void setup() {
        def threadDecorator1 = new ThreadDecorator(Mock(Runnable))
        threadDecorator1.status = Status.IS_FAILED
        def threadDecorator2 = new ThreadDecorator(Mock(Runnable))
        threadDecorator2.status = Status.IS_FINISHED
        def threadDecorator3 = new ThreadDecorator(Mock(Runnable))
        threadDecorator3.status = Status.IS_INTERRUPTED
        def threadDecorator4 = new ThreadDecorator(Mock(Runnable))
        collection = [threadDecorator1, threadDecorator2, threadDecorator3, threadDecorator4]
    }

    def "Get all finished, failed and interrupted tasks"() {
        given:
        def context = new ContextImpl(collection)

        when:
        def completed = context.getCompletedTaskCount()
        def failed = context.getFailedTaskCount()
        def interrupted = context.getInterruptedTaskCount()
        def isFinished = context.isFinished()

        then:
        completed == 1
        failed == 1
        interrupted == 1
        !isFinished

    }

    def "Interrupt"(){
        given:
        def context = new ContextImpl(collection)

        when:
        context.interrupt()

        then:
        collection[3].getStatus() == Status.IS_INTERRUPTED
    }

    def "Return finished" (){
        given:
        def threadDecorator2 = new ThreadDecorator(Mock(Runnable))
        threadDecorator2.status = Status.IS_FINISHED
        def threadDecorator3 = new ThreadDecorator(Mock(Runnable))
        threadDecorator3.status = Status.IS_INTERRUPTED
        def collectionFinished = [threadDecorator2, threadDecorator3]
        def context = new ContextImpl(collectionFinished)

        when:
        def result = context.isFinished()

        then:
        result
    }

    def "Get statistics" () {
        given:
        def threadDecorator2 = new ThreadDecorator(Mock(Runnable))
        threadDecorator2.status = Status.IS_FINISHED
        threadDecorator2.executionTime = 5
        def threadDecorator3 = new ThreadDecorator(Mock(Runnable))
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
