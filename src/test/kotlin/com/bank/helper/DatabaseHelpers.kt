package com.bank.helper

import org.jooq.DSLContext
import org.jooq.impl.DSL
import java.math.BigDecimal

fun DSLContext.createAccount(id: String, name: String) {
    this
        .insertInto(DSL.table("account"))
        .columns(
            DSL.field("id"),
            DSL.field("name")
        )
        .values(id, name)
        .execute()
}
