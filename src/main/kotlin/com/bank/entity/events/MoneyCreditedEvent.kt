package com.bank.entity.events

data class MoneyCreditedEvent(val accountId: String,
                              val amount: Double) : Event(accountId)
