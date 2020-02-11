package com.bank.services

import com.bank.store.AccountStore
import com.bank.store.entity.AccountEntity

open class AccountService(private val accountStore: AccountStore) {
    open fun getAccount(id: String) : AccountEntity {
        return accountStore.getAccount(id)
    }

}
