package com.bank.controller

import com.bank.helper.startServer
import com.bank.services.AccountService
import com.bank.store.entity.AccountEntity
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Provides
import com.nhaarman.mockito_kotlin.*
import io.restassured.RestAssured.given
import org.hamcrest.Matchers
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import spark.kotlin.stop

class AccountControllerTest {


    companion object {

        private const val ACCOUNT_ID = "account-id"
        private const val INVALID_ACCOUNT_ID = "invalid-account-id"


        private val accountService = mock<AccountService>()

        private var port: Int = 0

        @AfterClass
        @JvmStatic
        fun tearDown() {
            stop()
        }

        @BeforeClass
        @JvmStatic
        fun setup() {
            port = startServer {
                val injector = Guice.createInjector(TestModule(accountService))
                injector.injectMembers(AccountController())
            }
        }
    }

    @Before
    fun setUp() {
        reset(accountService)
    }


    @Test
    fun `getAccount - should 404 if the account does not exist`() {
        // given
        given(accountService.getAccount(any()))
            .willThrow(com.bank.base.exception.AccountNotFoundException(INVALID_ACCOUNT_ID))

        given()
            .port(port)
            .`when`()
            .get("/accounts/$INVALID_ACCOUNT_ID")
            .then()
            .statusCode(404)

        verify(accountService).getAccount(INVALID_ACCOUNT_ID)
    }

    @Test
    fun `getAccount - happy path`() {
        // given
        val expectedAccount = AccountEntity(
            id = ACCOUNT_ID,
            name = "Naruto Uzumaki",
            balance = 0.0
        )
        given(accountService.getAccount(any()))
            .willReturn(
                expectedAccount
            )

        given()
            .port(port)
            .`when`()
            .get("/accounts/$ACCOUNT_ID")
            .then()
            .statusCode(200)
            .body("id", Matchers.equalTo(ACCOUNT_ID))
            .body("name", Matchers.equalTo("Naruto Uzumaki"))

        verify(accountService).getAccount(ACCOUNT_ID)
    }


    private class TestModule(private val accountService: AccountService) : AbstractModule() {
        @Provides
        fun accountService() = accountService
    }
}