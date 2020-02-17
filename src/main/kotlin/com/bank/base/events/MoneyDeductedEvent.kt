package com.bank.base.events

data class MoneyDeductedEvent(val accountId: String,
                              val amount: Double) : Event(accountId)
