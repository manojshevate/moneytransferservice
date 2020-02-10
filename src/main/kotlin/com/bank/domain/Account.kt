package com.bank.domain

import com.bank.event.AccountCreatedEvent
import com.bank.event.Event
import com.bank.event.MoneyCreditedEvent
import com.bank.event.MoneyDeductedEvent

class Account {

    var name: String? = null
    var balance: Double = 0.0
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
        balance += event.amount
    }

    private fun apply(event: MoneyDeductedEvent) {
        id = event.accountId
        balance -= event.amount
    }
}
