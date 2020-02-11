package com.bank.store

import com.bank.helper.StoreTestBase
import com.bank.helper.TestDatabase
import com.bank.helper.createAccount
import com.bank.store.entity.AccountEntity
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

class AccountStoreTest : StoreTestBase() {

    companion object {
        private const val ACCOUNT_ID = "account-id"
    }

    private lateinit var subject: AccountStore

    @Before
    fun setup() {
        subject = AccountStore(TestDatabase.dataSource)
    }

    @Test
    fun `create - should create account in db`() {
        //given

        //when
        subject.createAccount(id = ACCOUNT_ID, name = "Naruto Uzumaki")

        //then
        dbCtx.fetchOne("select name from account where id='$ACCOUNT_ID'")
            .run {
                assertThat(this.get(0) as String).isEqualTo("Naruto Uzumaki")
            }
    }

    @Test
    fun `update - should update existing record in db`() {
        //given

        dbCtx.createAccount(ACCOUNT_ID, "Naruto Uzumaki")

        //when
        subject.updateAccount(id = "account-id", balance = 15.25)

        //then
        dbCtx.fetchOne("select balance from account where id='$ACCOUNT_ID'")
            .run {
                assertThat(this.get(0) as BigDecimal).isEqualTo(BigDecimal.valueOf(15.25))
            }
    }

    @Test
    fun `get - should fetch existing account details`() {
        //given

        dbCtx.createAccount(ACCOUNT_ID, "Naruto Uzumaki")

        //when
        val account = subject.getAccount(id = "account-id")

        //then
        assertThat(account).isEqualTo(AccountEntity(
            id = ACCOUNT_ID,
            name = "Naruto Uzumaki",
            balance = 0.0
        ))
    }

}