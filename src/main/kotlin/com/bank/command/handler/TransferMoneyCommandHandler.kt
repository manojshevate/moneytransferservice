package com.bank.command.handler
import com.bank.domain.Account
import com.bank.event.Event
import com.bank.exceptions.AccountNotFoundException
import com.bank.model.command.TransferMoneyCommand
import com.bank.event.MoneyCreditedEvent
import com.bank.event.MoneyDeductedEvent
import com.bank.exceptions.InsufficientFundsException
import com.bank.services.EventService
import com.bank.store.EventStore

open class TransferMoneyCommandHandler(private val eventStore: EventStore,
                                  private val eventService: EventService) {
    open fun handle(command: TransferMoneyCommand) {
        fetchAccount(command.to)
        val fromAccount = fetchAccount(command.from)

        if(fromAccount.balance < command.amount) {
            throw InsufficientFundsException(command.from)
        }

        MoneyDeductedEvent(command.from, command.amount).also { eventStore }

        raiseEvent(MoneyDeductedEvent(command.from, command.amount))
        raiseEvent(MoneyCreditedEvent(command.to, command.amount))
    }

    private fun raiseEvent(event: Event) {
            eventStore.save(event)
            eventService.send(event)
    }

    private fun fetchAccount(accountId: String): Account {
        return eventStore.fetchAll(accountId).also {
            if(it.isEmpty()) throw AccountNotFoundException(accountId)
        }.let {
            Account().applyAll(it)
        }
    }

}