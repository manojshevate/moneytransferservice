package com.bank.projections

import com.bank.base.events.TransactionCompletedEvent
import com.bank.store.TransactionStore
import com.google.common.eventbus.EventBus
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doNothing
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TransactionsProjectionTest {
    companion object {
        private const val FROM_ACCOUNT_ID = "from_account_id"
        private const val TO_ACCOUNT_ID = "to_account_id"
        private const val TRANSACTION_ID = "transaction_id"
        private const val AMOUNT = 10.0

        private const val TIME_OUT = 500L
    }

    private lateinit var eventBus: EventBus

    @Mock
    private lateinit var store : TransactionStore

    @InjectMocks
    private lateinit var subject : TransactionsProjection

    @Before
    fun setUp() {
        eventBus = EventBus()
        eventBus.register(subject)

        doNothing().`when`(store).insertTransaction(any(), any(), any(), any())
    }

    @Test
    fun `should handle account created event`() {
        //given
        val event =
            TransactionCompletedEvent(
                TRANSACTION_ID,
                FROM_ACCOUNT_ID,
                TO_ACCOUNT_ID,
                AMOUNT
            )

        //when
        eventBus.post(event)

        //then
        Thread.sleep(TIME_OUT)
        verify(store).insertTransaction(
            TRANSACTION_ID,
            FROM_ACCOUNT_ID,
            TO_ACCOUNT_ID,
            AMOUNT
        )
    }

}