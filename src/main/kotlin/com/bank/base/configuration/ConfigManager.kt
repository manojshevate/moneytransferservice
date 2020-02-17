package com.bank.base.configuration

import com.bank.controller.AccountController
import com.bank.controller.TransferController
import com.bank.event.listener.AccountEventListener
import com.bank.event.listener.TransactionEventListener
import com.bank.logger
import com.google.common.eventbus.EventBus
import com.google.inject.Guice
import com.google.inject.Injector
import org.flywaydb.core.Flyway
import org.jooq.impl.DSL
import javax.sql.DataSource

class ConfigManager {

    companion object {
        val init = {
            val injector = Guice.createInjector(
                AccountsModule(),
                DatabaseModule(),
                TransferModule()
            )
            injector.injectMembers(AccountController())
            injector.injectMembers(TransferController())


            registerEventListener(injector)
            databaseMigration(injector)
            prefillDataForTesting(injector)
        }

        private fun databaseMigration(injector: Injector) {
            injector.getInstance(Flyway::class.java).migrate()
        }

        private fun registerEventListener(injector: Injector) {
            val accountEventListener = injector.getInstance(AccountEventListener::class.java)
            val transactionEventListener = injector.getInstance(TransactionEventListener::class.java)
            injector.getInstance(EventBus::class.java).apply {
                register(accountEventListener)
                register(transactionEventListener)
            }
        }

        private fun prefillDataForTesting(injector: Injector) {
            insertDummyDataInDB(injector.getInstance(DataSource::class.java))
        }

        private fun insertDummyDataInDB(dataSource: DataSource) {
            logger.info("########### Started pre-filling the data")
            val accountsSeedDataSql = object {}.javaClass
                .classLoader
                .getResource("db/data/dummy_data.sql")?.readText()
            DSL.using(dataSource.connection)
                .execute(accountsSeedDataSql)
            logger.info("########### Finished pre-filling the data")
        }
    }
}