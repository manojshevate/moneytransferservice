package com.bank.event.listener

import com.bank.domain.Account
import com.bank.event.AccountCreatedEvent
import com.bank.event.Event
import com.bank.event.MoneyCreditedEvent
import com.bank.event.MoneyDeductedEvent
import com.bank.store.AccountStore
import com.bank.store.EventStore
import com.google.common.eventbus.Subscribe

open class AccountEventListener(private val accountStore: AccountStore,
                                private val eventStore: EventStore) {

    @Subscribe
    fun handleAccountCreation(event: AccountCreatedEvent) {
        accountStore.createAccount(event.accountId, event.name)
    }

    @Subscribe
    fun handleAccountCredit(event: MoneyCreditedEvent) {
        handleAccountBalanceUpdate(event.accountId, event)
    }

    @Subscribe
    fun handleAccountDebit(event: MoneyDeductedEvent) {
        handleAccountBalanceUpdate(event.accountId, event)
    }

    private fun handleAccountBalanceUpdate(accountId: String, event: Event) {
        eventStore.fetchAll(id = accountId)
            .plus(event)
            .let {
                Account().applyAll(it)
            }.also {
                accountStore.updateAccount(it.id!!, it.balance)
            }
    }

}
