package com.bank.controller

import com.bank.command.handler.TransferMoneyCommandHandler
import com.bank.helper.startServer
import com.bank.model.command.TransferMoneyCommand
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Provides
import com.nhaarman.mockito_kotlin.*
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.Matchers
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import spark.kotlin.stop
import javax.security.auth.login.AccountNotFoundException

class TransferControllerTest {


    companion object {

        private const val FROM_ACCOUNT_ID = "from-account-id"
        private const val TO_ACCOUNT_ID = "to-account-id"
        private const val INVALID_ACCOUNT_ID = "invalid-account-id"


        private val commandHandler = mock<TransferMoneyCommandHandler>()

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
                val injector = Guice.createInjector(TestModule(commandHandler))
                injector.injectMembers(TransferController())
            }
        }
    }

    @Before
    fun setUp() {
        reset(commandHandler)
    }

    @Test
    fun `transfer - should return 204 for successful transaction`() {
        val command = TransferMoneyCommand(
            from = FROM_ACCOUNT_ID,
            to = TO_ACCOUNT_ID,
            amount = 10.0
        )
        doNothing().`when`(commandHandler).handle(any())

        given()
            .contentType(ContentType.JSON)
            .body(
                """{
                        "from": "$FROM_ACCOUNT_ID",
                        "to": "$TO_ACCOUNT_ID",
                        "amount": 10.0
                    }""".trimMargin()
            )
            .port(port)
            .`when`()
            .post("/transfer")
            .then()
            .statusCode(204)


        // then
        verify(commandHandler).handle(command)
    }

    @Test
    fun `transfer - should return 400 for invalid account`() {
        val command = TransferMoneyCommand(
            from = INVALID_ACCOUNT_ID,
            to = TO_ACCOUNT_ID,
            amount = 10.0
        )

        doThrow(com.bank.exceptions.AccountNotFoundException(INVALID_ACCOUNT_ID))
            .`when`(commandHandler).handle(any())

        given()
            .contentType(ContentType.JSON)
            .body(
                """{
                        "from": "$INVALID_ACCOUNT_ID",
                        "to": "$TO_ACCOUNT_ID",
                        "amount": 10.0
                    }""".trimMargin()
            )
            .port(port)
            .`when`()
            .post("/transfer")
            .then()
            .statusCode(400)
            .body("errorCode", Matchers.equalTo("bad_request"))
            .body("errorMessage", Matchers.equalTo("Account id: $INVALID_ACCOUNT_ID not found"))


        // then
        verify(commandHandler).handle(command)
    }

    @Test
    fun `transfer - should return 400 for insufficient funds`() {
        val command = TransferMoneyCommand(
            from = FROM_ACCOUNT_ID,
            to = TO_ACCOUNT_ID,
            amount = 10.0
        )

        doThrow(com.bank.exceptions.InsufficientFundsException(FROM_ACCOUNT_ID))
            .`when`(commandHandler).handle(any())

        given()
            .contentType(ContentType.JSON)
            .body(
                """{
                        "from": "$FROM_ACCOUNT_ID",
                        "to": "$TO_ACCOUNT_ID",
                        "amount": 10.0
                    }""".trimMargin()
            )
            .port(port)
            .`when`()
            .post("/transfer")
            .then()
            .statusCode(400)
            .body("errorCode", Matchers.equalTo("bad_request"))
            .body("errorMessage", Matchers.equalTo("insufficient funds in account $FROM_ACCOUNT_ID"))


        // then
        verify(commandHandler).handle(command)
    }

    private class TestModule(private val commandHandler: TransferMoneyCommandHandler) : AbstractModule() {
        @Provides
        fun commandHandler() : TransferMoneyCommandHandler {
            return commandHandler
        }
    }
}