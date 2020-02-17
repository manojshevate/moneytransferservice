package com.bank.projections

import com.bank.base.events.AccountCreatedEvent
import com.bank.base.events.MoneyCreditedEvent
import com.bank.base.events.MoneyDeductedEvent
import com.bank.store.AccountStore
import com.bank.store.EventStore
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doNothing
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import com.google.common.eventbus.EventBus

@RunWith(MockitoJUnitRunner::class)
class AccountsProjectionTest {

    companion object {
        private const val ACCOUNT_ID = "account-id"
        private const val NAME = "name"
        private const val AMOUNT = 10.0

        private const val TIME_OUT = 500L
    }

    private lateinit var eventBus: EventBus

    @Mock
    private lateinit var accountStore : AccountStore

    @Mock
    private lateinit var eventStore: EventStore

    @InjectMocks
    private lateinit var subject : AccountsProjection

    @Before
    fun setUp() {
        eventBus = EventBus()
        eventBus.register(subject)

        doNothing().`when`(accountStore).createAccount(any(), any())
        doNothing().`when`(accountStore).updateAccount(any(), any())
    }

    @Test
    fun `should handle account created event`() {
        //given
        val event = AccountCreatedEvent(
            ACCOUNT_ID,
            NAME
        )

        //when
        eventBus.post(event)

        //then
        Thread.sleep(TIME_OUT)
        verify(accountStore).createAccount(
            ACCOUNT_ID,
            NAME
        )
    }

    @Test
    fun `should handle money credited event`() {
        //given
        val event = MoneyCreditedEvent(
            ACCOUNT_ID,
            AMOUNT
        )

        given(eventStore.fetchAll(any()))
            .willReturn(listOf(anAccountCreatedEvent(), event))

        //when
        eventBus.post(event)

        //then
        Thread.sleep(TIME_OUT)
        verify(accountStore).updateAccount(
            ACCOUNT_ID,
            AMOUNT
        )
    }

    @Test
    fun `should handle money deducted event`() {
        //given
        val event = MoneyDeductedEvent(
            ACCOUNT_ID,
            AMOUNT
        )

        given(eventStore.fetchAll(any()))
            .willReturn(listOf(anAccountCreatedEvent(), aMoneyCreditedEvent(), event))

        //when
        eventBus.post(event)

        //then
        Thread.sleep(TIME_OUT)
        verify(accountStore).updateAccount(ACCOUNT_ID, 0.0)
    }


    private fun anAccountCreatedEvent() : AccountCreatedEvent {
        return AccountCreatedEvent(
            accountId = ACCOUNT_ID,
            name = NAME
        )
    }

    private fun aMoneyCreditedEvent() : MoneyCreditedEvent {
        return MoneyCreditedEvent(
            accountId = ACCOUNT_ID,
            amount = AMOUNT
        )
    }
}