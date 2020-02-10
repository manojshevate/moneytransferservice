package com.bank.entity.events

data class AccountCreatedEvent(val accountId: String, val name: String) : Event(accountId)
