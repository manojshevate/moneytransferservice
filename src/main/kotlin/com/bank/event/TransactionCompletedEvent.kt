package com.bank.event

data class TransactionCompletedEvent(
    val transactionId: String,
    val fromAccount: String,
    val toAccount: String,
    val amount: Double
): Event(transactionId)