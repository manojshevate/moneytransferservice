package com.bank.event

data class MoneyCreditedEvent(val accountId: String,
                              val amount: Double) : Event(accountId)
