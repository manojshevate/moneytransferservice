package com.bank.store.entity

import org.jooq.DSLContext
import org.jooq.impl.DSL

data class EventEntity(val id: String = "",
                         val jsonValue: String = "")


fun EventEntity.store(dslContext: DSLContext) {
    dslContext.insertInto(DSL.table("event_store"))
        .columns(
            DSL.field("id"),
            DSL.field("json_value")
        )
        .values(
            this.id,
            this.jsonValue
        )
        .execute()
}