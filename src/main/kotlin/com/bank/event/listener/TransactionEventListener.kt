package com.bank.event.listener

import com.bank.event.TransactionCompletedEvent
import com.bank.store.TransactionStore
import com.google.common.eventbus.Subscribe

open class TransactionEventListener(private val store: TransactionStore) {

    @Subscribe
    fun handleTransactionCompletedEvent(event: TransactionCompletedEvent) {
        store.insertTransaction(event.transactionId, event.fromAccount, event.toAccount, event.amount)
    }

}
