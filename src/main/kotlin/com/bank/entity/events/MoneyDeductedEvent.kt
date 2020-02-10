package com.bank.entity.events

data class MoneyDeductedEvent(val accountId: String,
                              val amount: Double) : Event(accountId)
