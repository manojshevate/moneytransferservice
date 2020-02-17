package com.bank.store

import com.bank.store.entity.TransactionEntity
import com.bank.store.entity.store
import org.jooq.Configuration
import org.jooq.impl.DSL
import java.math.BigDecimal
import java.sql.Connection
import java.sql.Timestamp
import javax.sql.DataSource

open class TransactionStore(private val dataSource: DataSource) {
    open fun insertTransaction(id: String, from: String, to: String, amount: Double) {
        withConnection { connection ->
            DSL.using(connection)
                .transactionResult { config: Configuration ->
                    TransactionEntity(id, from, to, amount).store(config.dsl())
                }
        }
    }

    open fun getTransactionByAccountId(accountId: String): List<TransactionEntity> {
        return withConnection { connection ->
            val record = DSL.using(connection)
                .select(DSL.field("id"))
                .select(DSL.field("fromAccount"))
                .select(DSL.field("toAccount"))
                .select(DSL.field("amount"))
                .select(DSL.field("created_date"))
                .from(DSL.table("transaction_store"))
                .where(DSL.field("fromAccount").eq(accountId).or(DSL.field("toAccount").eq(accountId)))
                .orderBy(DSL.field("created_date"))
                .fetch()

            record.map {
                TransactionEntity(
                    id = it[0] as String,
                    fromAccount = it[1] as String,
                    toAccount = it[2] as String,
                    amount = (it[3] as BigDecimal).toDouble(),
                    createdDate = it[4] as Timestamp
                )
            }
        }
    }

    private fun <T>withConnection(op: (Connection) -> T) : T {
        val connection = dataSource.connection
        val response = op(connection)
        connection.close()
        return response
    }
}
