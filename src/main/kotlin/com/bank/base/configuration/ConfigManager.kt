package com.bank.base.configuration

import com.bank.controller.AccountController
import com.bank.controller.TransferController
import com.bank.event.AccountCreatedEvent
import com.bank.event.MoneyCreditedEvent
import com.bank.event.listener.AccountEventListener
import com.bank.logger
import com.bank.store.EventStore
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
            val eventListener = injector.getInstance(AccountEventListener::class.java)
            injector.getInstance(EventBus::class.java).register(eventListener)
        }

        private fun prefillDataForTesting(injector: Injector) {
            insertDummyEvents(injector.getInstance(EventStore::class.java))
            insertDummyDataInDB(injector.getInstance(DataSource::class.java))
        }

        private fun insertDummyDataInDB(dataSource: DataSource) {
            logger.info("########### Started pre-filling the data")
            val accountsSeedDataSql = object {}.javaClass
                .classLoader
                .getResource("db.data/accounts.sql")?.readText()
            DSL.using(dataSource.connection)
                .execute(accountsSeedDataSql)
            logger.info("########### Finished pre-filling the data")
        }

        private fun insertDummyEvents(eventStore: EventStore) {
            logger.info("########### Started inserting dummy events to store")
            eventStore.save(AccountCreatedEvent(accountId = "cc856b46-4c6e-11ea-b77f-2e728ce88125", name = "Naruto"))
            eventStore.save(AccountCreatedEvent(accountId = "d0c91658-4c6e-11ea-b77f-2e728ce88125", name = "Sakura"))

            eventStore.save(MoneyCreditedEvent(accountId = "cc856b46-4c6e-11ea-b77f-2e728ce88125", amount = 12345.00))
            eventStore.save(MoneyCreditedEvent(accountId = "d0c91658-4c6e-11ea-b77f-2e728ce88125", amount = 12345.00))
            logger.info("########### Finished inserting dummy events to store")
        }
    }
}