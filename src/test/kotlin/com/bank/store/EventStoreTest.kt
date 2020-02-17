package com.bank.store

import com.bank.base.events.AccountCreatedEvent
import com.bank.base.events.MoneyCreditedEvent
import com.bank.helper.StoreTestBase
import com.bank.helper.TestDatabase
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class EventStoreTest : StoreTestBase(){


    companion object {
        private const val ACCOUNT_ID = "account-id"
        private const val ACCOUNT_ID_2 = "account-id-2"
        private const val ACCOUNT_NAME = "Naruto Uzumaki"
        private const val AMOUNT = 10.0
    }

    private val subject: EventStore = EventStore(TestDatabase.dataSource)

    @Test
    fun `should return empty list when no matching event present`() {
        //given

        //when
        val result = subject.fetchAll(id = ACCOUNT_ID)

        //then
         assertThat(result).isNullOrEmpty()
    }

    @Test
    fun `should return one event list after saving one event`() {
        //given
        subject.save(anAccountCreatedEvent(ACCOUNT_ID))
        subject.save(anAccountCreatedEvent(ACCOUNT_ID_2))

        //when
        val result = subject.fetchAll(id = ACCOUNT_ID)

        //then
        assertThat(result?.size).isEqualTo(1)
    }

    @Test
    fun `should return multiple events after saving multiple events`() {
        //given

        subject.save(anAccountCreatedEvent(ACCOUNT_ID))
        subject.save(aMoneyCreditedEvent(ACCOUNT_ID))
        subject.save(anAccountCreatedEvent(ACCOUNT_ID_2))

        //when
        val result = subject.fetchAll(id = ACCOUNT_ID)

        //then
        assertThat(result?.size).isEqualTo(2)
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