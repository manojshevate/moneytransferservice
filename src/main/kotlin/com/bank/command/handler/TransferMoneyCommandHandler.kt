package com.bank.command.handler
import com.bank.base.exception.AccountNotFoundException
import com.bank.base.exception.InsufficientFundsException
import com.bank.command.TransferMoneyCommand
import com.bank.controller.dto.MoneyTransferResponse
import com.bank.domain.Account
import com.bank.base.events.Event
import com.bank.base.events.MoneyCreditedEvent
import com.bank.base.events.MoneyDeductedEvent
import com.bank.base.events.TransactionCompletedEvent
import com.bank.services.EventService
import com.bank.store.EventStore
import java.util.*

open class TransferMoneyCommandHandler(private val eventStore: EventStore,
                                  private val eventService: EventService) {
    open fun handle(command: TransferMoneyCommand) : MoneyTransferResponse {
        fetchAccount(command.to)
        val fromAccount = fetchAccount(command.from)

        if(fromAccount.balance < command.amount) {
            throw InsufficientFundsException(command.from)
        }

        val transactionId = UUID.randomUUID().toString()

        raiseEvent(MoneyDeductedEvent(command.from, command.amount))
        raiseEvent(MoneyCreditedEvent(command.to, command.amount))
        raiseEvent(
            TransactionCompletedEvent(
                transactionId,
                command.from,
                command.to,
                command.amount
            )
        )

        return MoneyTransferResponse(transactionId)
    }

    private fun raiseEvent(event: Event) {
            eventStore.save(event)
            eventService.send(event)
    }

    private fun fetchAccount(accountId: String): Account {
        return eventStore.fetchAll(accountId).also {
            if(it.isNullOrEmpty()) throw AccountNotFoundException(accountId)
        }.let {
            Account().applyAll(it!!)
        }
    }

}