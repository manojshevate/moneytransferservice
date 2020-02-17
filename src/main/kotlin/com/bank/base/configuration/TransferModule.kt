package com.bank.base.configuration

import com.bank.command.handler.TransferMoneyCommandHandler
import com.bank.projections.AccountsProjection
import com.bank.projections.TransactionsProjection
import com.bank.services.EventService
import com.bank.store.AccountStore
import com.bank.store.EventStore
import com.bank.store.TransactionStore
import com.google.common.eventbus.EventBus
import com.google.inject.Exposed
import com.google.inject.PrivateModule
import com.google.inject.Provides
import com.google.inject.Singleton

class TransferModule : PrivateModule() {
    companion object {
        private val eventBus = EventBus()
    }
    override fun configure() {
    }

    @Provides
    @Singleton
    @Exposed
    fun eventBus(): EventBus {
        return eventBus
    }

    @Provides
    @Singleton
    @Exposed
    fun accountEventListener(accountStore: AccountStore, eventStore: EventStore): AccountsProjection {
        return AccountsProjection(accountStore, eventStore)
    }

    @Provides
    @Singleton
    @Exposed
    fun transactionEventListener(store: TransactionStore): TransactionsProjection {
        return TransactionsProjection(store)
    }

    @Provides
    @Singleton
    @Exposed
    fun eventService(): EventService {
        return EventService(eventBus)
    }

    @Provides
    @Singleton
    @Exposed
    fun commandHandler(eventStore: EventStore, eventService: EventService): TransferMoneyCommandHandler {
        return TransferMoneyCommandHandler(eventStore, eventService)
    }
}
