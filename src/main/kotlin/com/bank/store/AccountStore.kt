package com.bank.store

import com.bank.base.exception.AccountNotFoundException
import com.bank.store.entity.AccountEntity
import com.bank.store.entity.store
import com.bank.store.entity.updateBalance
import org.jooq.Configuration
import org.jooq.impl.DSL
import java.math.BigDecimal
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

    open fun getAccount(id: String): AccountEntity {
        val record = DSL.using(dataSource.connection)
            .select(DSL.field("balance"))
            .select(DSL.field("name"))
            .from(DSL.table("account"))
            .where(DSL.field("id").eq(id))
            .fetchOne() ?: throw AccountNotFoundException(id)
        return record.map {
            AccountEntity(
                id = id,
                balance = (it[0] as BigDecimal).toDouble(),
                name = it[1] as String
            )
        }
    }
}
