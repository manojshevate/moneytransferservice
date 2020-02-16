package com.bank.helper

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.FluentConfiguration
import javax.sql.DataSource

class TestDatabase {
    companion object {
        val dataSource: DataSource by lazy {
            HikariDataSource(
                HikariConfig().apply {
                    jdbcUrl = "jdbc:h2:mem:test"
                    username = "sa"
                    maximumPoolSize = 20
                    connectionTimeout = 1000
                }
            )
        }

        fun initDb() {
            Flyway(FluentConfiguration().dataSource(dataSource)).run {
                clean()
                migrate()
            }
        }
    }
}
