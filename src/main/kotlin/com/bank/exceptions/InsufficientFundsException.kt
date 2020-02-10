package com.bank.exceptions

import java.lang.RuntimeException

class InsufficientFundsException(accountId: String) : RuntimeException("insufficient funds in account $accountId")
