package com.bank.projections

import com.bank.domain.Account
import com.bank.base.events.AccountCreatedEvent
import com.bank.base.events.MoneyCreditedEvent
import com.bank.base.events.MoneyDeductedEvent
import com.bank.store.AccountStore
import com.bank.store.EventStore
import com.google.common.eventbus.Subscribe

open class AccountsProjection(private val accountStore: AccountStore,
                              private val eventStore: EventStore) {

    @Subscribe
    fun handleAccountCreation(event: AccountCreatedEvent) = accountStore.createAccount(event.accountId, event.name)

    @Subscribe
    fun handleAccountCredit(event: MoneyCreditedEvent) = handleAccountBalanceUpdate(event.accountId)

    @Subscribe
    fun handleAccountDebit(event: MoneyDeductedEvent) = handleAccountBalanceUpdate(event.accountId)

    private fun handleAccountBalanceUpdate(accountId: String) {
        eventStore.fetchAll(id = accountId)
            .let {
                Account().applyAll(it!!)
            }.also {
                accountStore.updateAccount(it.id!!, it.balance)
            }
    }

}
