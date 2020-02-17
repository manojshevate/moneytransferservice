package com.bank.store

import com.bank.helper.StoreTestBase
import com.bank.helper.TestDatabase
import org.assertj.core.api.Assertions
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TransactionStoreTest : StoreTestBase() {

    private lateinit var subject: TransactionStore

    @Before
    fun setup() {
        subject = TransactionStore(TestDatabase.dataSource)
    }

    @Test
    fun `create - should insert transaction in db`() {
        //given

        //when
        subject.insertTransaction(id = "transaction-id", from = "from-account", to = "to-account", amount = 10.0)

        //then
        dbCtx.fetchOne("select fromAccount, toAccount from transaction_store where id='transaction-id'")
            .run {
                Assertions.assertThat(this.get(0) as String).isEqualTo("from-account")
                Assertions.assertThat(this.get(1) as String).isEqualTo("to-account")
            }
    }

    @Test
    fun `getTransactionsByAccountId -  should return empty list for no transactions`() {
        //given

        //when
        val result = subject.getTransactionByAccountId("unknown-account-id")

        //then
        Assertions.assertThat(result).isEmpty()
    }

    @Test
    fun `getTransactionsByAccountId -  should return  transactions if found`() {
        //given
        subject.insertTransaction(id = "transaction-id", from = "from-account", to = "to-account", amount = 10.0)
        subject.insertTransaction(id = "transaction-id", to = "from-account", from = "to-account", amount = 10.0)

        //when
        val result = subject.getTransactionByAccountId("from-account")

        //then
        Assertions.assertThat(result.size).isEqualTo(2)
    }
}