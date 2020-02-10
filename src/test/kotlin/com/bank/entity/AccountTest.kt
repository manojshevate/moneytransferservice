package com.bank.entity

import com.bank.entity.events.AccountCreatedEvent
import com.bank.entity.events.MoneyCreditedEvent
import com.bank.entity.events.MoneyDeductedEvent
import org.assertj.core.api.Assertions
import org.junit.Test

class AccountTest {

    companion object {
        private const val ACCOUNT_ID = "account-id"
        private const val ACCOUNT_NAME = "Naruto Uzumaki"
        private const val AMOUNT = 10.0
    }

    @Test
    fun `should create new and empty account`() {
        //given
        //when
        val account = Account()

        //then
        Assertions.assertThat(account.id).isNull()
        Assertions.assertThat(account.name).isNull()
        Assertions.assertThat(account.amount).isEqualTo(0.0)
    }

    @Test
    fun `should apply AccountCreated Event`() {
        //given
        //when
        val account = Account().applyAll(listOf(
            anAccountCreatedEvent()
        ))

        //then
        Assertions.assertThat(account.id).isEqualTo(ACCOUNT_ID)
        Assertions.assertThat(account.name).isEqualTo(ACCOUNT_NAME)
        Assertions.assertThat(account.amount).isEqualTo(0.0)
    }

    @Test
    fun `should apply MoneyCredited Event`() {
        //given
        //when
        val account = Account().applyAll(listOf(
           anAccountCreatedEvent(),
            aMoneyCreditedEvent(),
            aMoneyCreditedEvent()
        ))

        //then
        Assertions.assertThat(account.id).isEqualTo(ACCOUNT_ID)
        Assertions.assertThat(account.name).isEqualTo(ACCOUNT_NAME)
        Assertions.assertThat(account.amount).isEqualTo(AMOUNT * 2)
    }

    @Test
    fun `should apply MoneyDeducted Event`() {
        //given
        //when
        val account = Account().applyAll(listOf(
           anAccountCreatedEvent(),
            aMoneyDeductedEvent()
        ))

        //then
        Assertions.assertThat(account.id).isEqualTo(ACCOUNT_ID)
        Assertions.assertThat(account.name).isEqualTo(ACCOUNT_NAME)
        Assertions.assertThat(account.amount).isEqualTo(AMOUNT * -1)
    }

    private fun anAccountCreatedEvent() : AccountCreatedEvent {
        return AccountCreatedEvent(
            accountId = ACCOUNT_ID,
            name = ACCOUNT_NAME
        )
    }

    private fun aMoneyCreditedEvent() : MoneyCreditedEvent {
        return MoneyCreditedEvent(
            accountId = ACCOUNT_ID,
            amount = AMOUNT
        )
    }

    private fun aMoneyDeductedEvent() : MoneyDeductedEvent {
        return MoneyDeductedEvent(
            accountId = ACCOUNT_ID,
            amount = AMOUNT
        )
    }
}