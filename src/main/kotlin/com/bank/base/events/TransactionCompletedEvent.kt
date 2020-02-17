package com.bank.base.events

data class TransactionCompletedEvent(
    val transactionId: String,
    val fromAccount: String,
    val toAccount: String,
    val amount: Double
): Event(transactionId)