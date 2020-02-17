package com.bank.store.entity

import org.jooq.DSLContext
import org.jooq.impl.DSL
import java.sql.Timestamp

data class TransactionEntity(val id: String = "",
                             val fromAccount: String = "",
                             val toAccount: String = "",
                             val amount: Double = 0.0,
                             val createdDate: Timestamp? = null)


fun TransactionEntity.store(dslContext: DSLContext) {
    dslContext.insertInto(DSL.table("transaction_store"))
        .columns(
            DSL.field("id"),
            DSL.field("fromAccount"),
            DSL.field("toAccount"),
            DSL.field("amount")
        )
        .values(
            this.id,
            this.fromAccount,
            this.toAccount,
            this.amount
        )
        .execute()
}