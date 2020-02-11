package com.bank.bean

import com.bank.command.handler.TransferMoneyCommandHandler
import com.bank.event.listener.AccountEventListener
import com.bank.services.EventService
import com.bank.store.AccountStore
import com.bank.store.EventStore
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
    fun eventListener(accountStore: AccountStore, eventStore: EventStore): AccountEventListener {
        return AccountEventListener(accountStore, eventStore)
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
