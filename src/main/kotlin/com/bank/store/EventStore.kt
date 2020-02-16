package com.bank.store

import com.bank.event.Event
import com.bank.store.entity.EventEntity
import com.bank.store.entity.store
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.jooq.Configuration
import org.jooq.impl.DSL
import java.sql.Connection
import javax.sql.DataSource


open class EventStore(private val dataSource: DataSource) {

    companion object {
        private val objectMapper = jacksonObjectMapper()
    }

    open fun fetchAll(id: String): List<Event>? {

        return withConnection { connection ->
            val records: MutableList<Event>? = DSL.using(connection)
                .select(DSL.field("json_value"))
                .from(DSL.table("event_store"))
                .where(DSL.field("id").eq(id))
                .orderBy(DSL.field("created_date"))
                .fetch()?.map {
                    val json = it[0] as String
                    objectMapper.readValue(json, Event::class.java)!!
                }
            records
        }

    }

    open fun save(event: Event) {
        withConnection { connection ->
            DSL.using(connection)
                .transactionResult { config: Configuration ->
                    EventEntity(event.id, objectMapper.writeValueAsString(event)).store(config.dsl())
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


