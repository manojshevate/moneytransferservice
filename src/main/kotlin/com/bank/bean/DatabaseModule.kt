package com.bank.bean

import com.bank.store.AccountStore
import com.bank.store.EventStore
import com.google.common.eventbus.EventBus
import com.google.inject.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.FluentConfiguration
import javax.sql.DataSource

class DatabaseModule : PrivateModule() {
    override fun configure() {
    }

    @Provides
    @Singleton
    @Exposed
    fun datasource(): DataSource = HikariDataSource(
        HikariConfig().apply {
            jdbcUrl = "jdbc:h2:mem:test"
            username = "sa"
            maximumPoolSize = 10
        }
    )

    @Provides
    @Singleton
    @Exposed
    fun flyway(dataSource: DataSource) = Flyway(FluentConfiguration().dataSource(dataSource))


    @Provides
    @Singleton
    @Exposed
    fun accountStore(dataSource: DataSource): AccountStore {
        return AccountStore(dataSource)
    }

    @Provides
    @Singleton
    @Exposed
    fun eventStore(): EventStore {
        return EventStore()
    }

}
