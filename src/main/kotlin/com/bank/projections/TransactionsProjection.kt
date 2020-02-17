package com.bank.projections

import com.bank.base.events.TransactionCompletedEvent
import com.bank.store.TransactionStore
import com.google.common.eventbus.Subscribe

open class TransactionsProjection(private val store: TransactionStore) {

    @Subscribe
    fun handleTransactionCompletedEvent(event: TransactionCompletedEvent) {
        store.insertTransaction(event.transactionId, event.fromAccount, event.toAccount, event.amount)
    }

}
