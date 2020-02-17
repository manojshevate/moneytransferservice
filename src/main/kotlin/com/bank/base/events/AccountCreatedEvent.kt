package com.bank.base.events

data class AccountCreatedEvent(val accountId: String, val name: String) : Event(accountId)
