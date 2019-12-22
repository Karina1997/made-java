package com.made

import spock.lang.Specification

class ThreadDecoratorSpec extends Specification {
    def "Run"() {
        given:
        def runnable = Mock(Runnable)
        def threadDecorator = new ThreadWithStatus(runnable)

        when:
        threadDecorator.run()

        then:
        1 * runnable.run()
        threadDecorator.getStatus() == Status.IS_FINISHED
    }

    def "Run with exception"() {
        given:
        def runnable = Mock(Runnable)
        def threadDecorator = new ThreadWithStatus(runnable)

        when:
        threadDecorator.run()

        then:
        1 * runnable.run() >> { throw new RuntimeException() }
        threadDecorator.getStatus() == Status.IS_FAILED
    }
}
