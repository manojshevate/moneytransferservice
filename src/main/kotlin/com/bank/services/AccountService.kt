package com.bank.services

import com.bank.controller.dto.Transaction
import com.bank.controller.dto.Type
import com.bank.store.AccountStore
import com.bank.store.TransactionStore
import com.bank.store.entity.AccountEntity

open class AccountService(private val accountStore: AccountStore,
                          private val transactionStore: TransactionStore) {
    open fun getAccount(id: String) : AccountEntity {
        return accountStore.getAccount(id)
    }

    open fun getTransactionsByAccountId(id: String) : List<Transaction> {
        return transactionStore.getTransactionByAccountId(id).map {
            Transaction(
                id = it.id,
                fromAccount = it.fromAccount,
                toAccount = it.toAccount,
                amount = it.amount,
                createdDate = it.createdDate.toString(),
                type = if (it.fromAccount == id) Type.DEBIT else Type.CREDIT
            )
        }
    }

}
