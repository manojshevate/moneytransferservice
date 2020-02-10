package com.bank.store

import com.bank.entity.events.AccountCreatedEvent
import com.bank.entity.events.MoneyCreditedEvent
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class EventStoreTest {


    companion object {
        private const val ACCOUNT_ID = "account-id"
        private const val ACCOUNT_ID_2 = "account-id-2"
        private const val ACCOUNT_NAME = "Naruto Uzumaki"
        private const val AMOUNT = 10.0
    }

    private val eventStore: EventStore = EventStore()

    @Before
    internal fun setUp() {
        eventStore.clearAll()
    }

    @Test
    fun `should return empty list when no matching event present`() {
        //given

        //when
        val result = eventStore.fetchAll(id = ACCOUNT_ID)

        //then
         assertThat(result).isEmpty()
    }

    @Test
    fun `should return one event list after saving one event`() {
        //given
        eventStore.save(anAccountCreatedEvent(ACCOUNT_ID))
        eventStore.save(anAccountCreatedEvent(ACCOUNT_ID_2))

        //when
        val result = eventStore.fetchAll(id = ACCOUNT_ID)

        //then
        assertThat(result.size).isEqualTo(1)
    }

    @Test
    fun `should return multiple events after saving multiple events`() {
        //given

        eventStore.save(anAccountCreatedEvent(ACCOUNT_ID))
        eventStore.save(aMoneyCreditedEvent(ACCOUNT_ID))
        eventStore.save(anAccountCreatedEvent(ACCOUNT_ID_2))

        //when
        val result = eventStore.fetchAll(id = ACCOUNT_ID)

        //then
        assertThat(result.size).isEqualTo(2)
    }

    private fun anAccountCreatedEvent(accountId: String) : AccountCreatedEvent {
        return AccountCreatedEvent(
            accountId = accountId,
            name = ACCOUNT_NAME
        )
    }

    private fun aMoneyCreditedEvent(accountId: String) : MoneyCreditedEvent {
        return MoneyCreditedEvent(
            accountId = accountId,
            amount = AMOUNT
        )
    }
}