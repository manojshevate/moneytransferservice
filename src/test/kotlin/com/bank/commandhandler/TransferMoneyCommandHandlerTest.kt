package com.bank.commandhandler

import com.bank.entity.events.AccountCreatedEvent
import com.bank.exceptions.AccountNotFoundException
import com.bank.exceptions.InsufficientFundsException
import com.bank.model.command.TransferMoneyCommand
import com.bank.entity.events.MoneyCreditedEvent
import com.bank.entity.events.MoneyDeductedEvent
import com.bank.services.EventService
import com.bank.store.EventStore
import com.nhaarman.mockito_kotlin.*
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TransferMoneyCommandHandlerTest {

    companion object {
        private const val FROM_ACCOUNT_ID = "from_account_id"
        private const val TO_ACCOUNT_ID = "to_account_id"
        private const val ACCOUNT_NAME = "Naruto Uzumaki"
        private const val AMOUNT = 100.0
    }

    @Mock
    private lateinit var eventStore: EventStore


    @Mock
    private lateinit var eventService: EventService

    @InjectMocks
    private lateinit var transferMoneyCommandHandler: TransferMoneyCommandHandler

    @Before
    fun setUp() {
        given(eventStore.fetchAll(any()))
            .willReturn(emptyList())

    }

    @Test
    fun `handle - should throw exception when no events found for to account`() {
        //given
        val command = TransferMoneyCommand(
            from = FROM_ACCOUNT_ID,
            to = TO_ACCOUNT_ID,
            amount = 100.56
        )
        //when
        val result = assertThatThrownBy {
            transferMoneyCommandHandler.handle(command)
        }

        //then
        result
            .isInstanceOf(AccountNotFoundException::class.java)

        verify(eventStore).fetchAll(TO_ACCOUNT_ID)
        verifyNoMoreInteractions(eventStore)
    }

    @Test
    fun `handle - should throw exception when no events found for from account`() {
        //given

        given(eventStore.fetchAll(TO_ACCOUNT_ID))
            .willReturn(listOf(anAccountCreatedEvent(TO_ACCOUNT_ID)))

        val command = TransferMoneyCommand(
            from = FROM_ACCOUNT_ID,
            to = TO_ACCOUNT_ID,
            amount = 100.56
        )
        //when
        val result = assertThatThrownBy {
            transferMoneyCommandHandler.handle(command)
        }

        //then
        result
            .isInstanceOf(AccountNotFoundException::class.java)

        verify(eventStore).fetchAll(TO_ACCOUNT_ID)
        verify(eventStore).fetchAll(FROM_ACCOUNT_ID)
    }

    @Test
    fun `handle - should throw exception when account balance in from account is less than transaction amount`() {
        //given

        given(eventStore.fetchAll(FROM_ACCOUNT_ID))
            .willReturn(listOf(anAccountCreatedEvent(FROM_ACCOUNT_ID)))

        given(eventStore.fetchAll(TO_ACCOUNT_ID))
            .willReturn(listOf(anAccountCreatedEvent(TO_ACCOUNT_ID)))

        val command = TransferMoneyCommand(
            from = FROM_ACCOUNT_ID,
            to = TO_ACCOUNT_ID,
            amount = 100.56
        )
        //when
        val result = assertThatThrownBy {
            transferMoneyCommandHandler.handle(command)
        }

        //then
        result
            .isInstanceOf(InsufficientFundsException::class.java)

        verify(eventStore).fetchAll(TO_ACCOUNT_ID)
        verify(eventStore).fetchAll(FROM_ACCOUNT_ID)
    }

    @Test
    fun `handle - should send MoneyCredited to "TO" account and Money deducted from "FROM" account events`() {
        //given

        doNothing().`when`(eventService).send(any())

        given(eventStore.fetchAll(FROM_ACCOUNT_ID))
            .willReturn(listOf(
                anAccountCreatedEvent(FROM_ACCOUNT_ID),
                aMoneyCreditedEvent(FROM_ACCOUNT_ID)
            ))

        given(eventStore.fetchAll(TO_ACCOUNT_ID))
            .willReturn(listOf(anAccountCreatedEvent(TO_ACCOUNT_ID)))

        val command = TransferMoneyCommand(
            from = FROM_ACCOUNT_ID,
            to = TO_ACCOUNT_ID,
            amount = 90.0
        )
        //when
        transferMoneyCommandHandler.handle(command)

        //then

        verify(eventStore).fetchAll(TO_ACCOUNT_ID)
        verify(eventStore).fetchAll(FROM_ACCOUNT_ID)
        verify(eventService).send(MoneyDeductedEvent(FROM_ACCOUNT_ID, 90.0))
        verify(eventService).send(MoneyCreditedEvent(TO_ACCOUNT_ID, 90.0))
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