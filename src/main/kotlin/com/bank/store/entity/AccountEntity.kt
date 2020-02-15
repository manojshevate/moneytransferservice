package com.bank.store.entity

import org.jooq.DSLContext
import org.jooq.impl.DSL

data class AccountEntity(val id: String = "",
                         val name: String = "",
                         val balance: Double = 0.0)


fun AccountEntity.store(dslContext: DSLContext) {
    dslContext.insertInto(DSL.table("account"))
        .columns(
            DSL.field("id"),
            DSL.field("name"),
            DSL.field("balance")
        )
        .values(
            this.id,
            this.name,
            this.balance
        )
        .execute()
}

fun AccountEntity.updateBalance(dslContext: DSLContext) {
    dslContext.update(DSL.table("account"))
        .set(DSL.field("balance"), balance)
        .where(DSL.field("id").eq(id))
        .execute()
}