package com.bank.entity

import com.bank.entity.events.AccountCreatedEvent
import com.bank.entity.events.Event
import com.bank.entity.events.MoneyCreditedEvent
import com.bank.entity.events.MoneyDeductedEvent

class Account {

    var name: String? = null
    var amount: Double = 0.0
    var id: String? = null

    fun applyAll(events: List<Event>): Account {
        events.forEach {
            when(it) {
                  is AccountCreatedEvent -> apply(it)
                  is MoneyCreditedEvent -> apply(it)
                  is MoneyDeductedEvent -> apply(it)
            }
        }
        return this
    }

    private fun apply(event: AccountCreatedEvent) {
        id = event.accountId
        name = event.name
    }

    private fun apply(event: MoneyCreditedEvent) {
        id = event.accountId
        amount += event.amount
    }

    private fun apply(event: MoneyDeductedEvent) {
        id = event.accountId
        amount -= event.amount
    }
}
