package com.bank.commandhandler
import com.bank.entity.Account
import com.bank.exceptions.AccountNotFoundException
import com.bank.model.command.TransferMoneyCommand
import com.bank.entity.events.MoneyCreditedEvent
import com.bank.entity.events.MoneyDeductedEvent
import com.bank.exceptions.InsufficientFundsException
import com.bank.services.EventService
import com.bank.store.EventStore

class TransferMoneyCommandHandler(private val eventStore: EventStore,
                                  private val eventService: EventService) {
    fun handle(command: TransferMoneyCommand) {
        fetchAccount(command.to)
        val fromAccount = fetchAccount(command.from)

        if(fromAccount.amount < command.amount) {
            throw InsufficientFundsException(command.from)
        }

        eventService.send(MoneyDeductedEvent(command.from, command.amount))
        eventService.send(MoneyCreditedEvent(command.to, command.amount))
    }

    private fun fetchAccount(accountId: String): Account {
        return eventStore.fetchAll(accountId).also {
            if(it.isEmpty()) throw AccountNotFoundException(accountId)
        }.let {
            Account().applyAll(it)
        }
    }

}