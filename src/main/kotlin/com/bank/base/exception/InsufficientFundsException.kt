package com.bank.base.exception

class InsufficientFundsException(accountId: String) : RuntimeException("insufficient funds in account $accountId")
