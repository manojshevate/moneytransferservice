package com.bank.services

import com.bank.event.AccountCreatedEvent
import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import com.nhaarman.mockito_kotlin.verify
import org.assertj.core.api.Assertions
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class EventServiceTest {

    private lateinit var subject : EventService
    @Before
    fun setUp() {
        val eventBus = EventBus()
        eventBus.register(EventListerStub())
        subject = EventService(eventBus)
    }

    @Test
    fun `send - happy path`() {
        //given

        //when
        subject.send(AccountCreatedEvent(accountId = "1", name = "Naruto Uzumaki"))
        subject.send(AccountCreatedEvent(accountId = "1", name = "Naruto Uzumaki"))

        //then
        Thread.sleep(500)
        Assertions.assertThat(EventListerStub.eventCount).isEqualTo(2)
    }
}

class EventListerStub {

    companion object {
        var eventCount = 0
    }

    @Subscribe
    fun handler(event: AccountCreatedEvent) {
        eventCount++
    }
}