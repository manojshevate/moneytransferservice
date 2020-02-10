package com.bank.event

data class AccountCreatedEvent(val accountId: String, val name: String) : Event(accountId)
