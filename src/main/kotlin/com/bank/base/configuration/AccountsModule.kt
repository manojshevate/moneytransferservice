package com.bank.base.configuration

import com.bank.services.AccountService
import com.bank.store.AccountStore
import com.bank.store.TransactionStore
import com.google.inject.Exposed
import com.google.inject.PrivateModule
import com.google.inject.Provides
import com.google.inject.Singleton

class AccountsModule : PrivateModule() {

    override fun configure() {
    }

    @Provides
    @Singleton
    @Exposed
    fun accountService(accountStore: AccountStore, transactionStore: TransactionStore): AccountService {
        return AccountService(accountStore, transactionStore)
    }

}
