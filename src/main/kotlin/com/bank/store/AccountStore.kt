package com.bank.store

import com.bank.store.entity.AccountEntity
import com.bank.store.entity.store
import com.bank.store.entity.updateBalance
import org.jooq.Configuration
import org.jooq.impl.DSL
import javax.sql.DataSource

open class AccountStore(private val dataSource: DataSource) {
    open fun createAccount(id: String, name: String) {
        DSL.using(dataSource.connection)
            .transactionResult { config: Configuration ->
                AccountEntity(id, name).store(config.dsl())
            }
    }

    open fun updateAccount(id: String, balance: Double) {
        DSL.using(dataSource.connection)
            .transactionResult { config: Configuration ->
                AccountEntity(id = id, balance = balance).updateBalance(config.dsl())
            }
    }
}
