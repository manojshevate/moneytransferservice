package com.bank.exceptions

import java.lang.RuntimeException

class AccountNotFoundException(accountId: String) : RuntimeException("Account id: $accountId not found")
