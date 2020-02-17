package com.bank.base.configuration

import com.bank.store.AccountStore
import com.bank.store.EventStore
import com.bank.store.TransactionStore
import com.google.inject.Exposed
import com.google.inject.PrivateModule
import com.google.inject.Provides
import com.google.inject.Singleton
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
            maximumPoolSize = 20
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
    fun transactionStore(dataSource: DataSource): TransactionStore {
        return TransactionStore(dataSource)
    }

    @Provides
    @Singleton
    @Exposed
    fun eventStore(dataSource: DataSource): EventStore {
        return EventStore(dataSource)
    }

}
