package com.bank.base.exception

class AccountNotFoundException(accountId: String) : RuntimeException("Account id: $accountId not found")
