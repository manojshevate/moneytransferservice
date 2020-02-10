package com.bank.event

data class MoneyDeductedEvent(val accountId: String,
                              val amount: Double) : Event(accountId)
