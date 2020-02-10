package com.bank.helper

import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.junit.Before

abstract class StoreTestBase {

    protected lateinit var dbCtx: DSLContext

    @Before
    fun setUp() {
        TestDatabase.initDb()
        dbCtx = DSL.using(TestDatabase.dataSource, SQLDialect.H2)
    }
}
