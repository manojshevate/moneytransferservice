package com.bank.base.events

data class MoneyCreditedEvent(val accountId: String,
                              val amount: Double) : Event(accountId)
