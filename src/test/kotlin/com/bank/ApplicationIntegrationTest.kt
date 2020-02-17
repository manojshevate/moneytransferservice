package com.bank

import com.bank.helper.startServer
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.response.ValidatableResponse
import org.hamcrest.Matchers
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import spark.kotlin.stop

class ApplicationIntegrationTest {


    companion object {

        private const val FROM_ACCOUNT_ID = "cc856b46-4c6e-11ea-b77f-2e728ce88125"
        private const val TO_ACCOUNT_ID = "d0c91658-4c6e-11ea-b77f-2e728ce88125"

        private const val REPEAT_COUNT = 9

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
                main()
            }
        }
    }

    @Before
    fun setUp() {
    }


    @Test
    fun `Verify balance before and after few transactions`() {
        getAccount(FROM_ACCOUNT_ID).assertBalance(12345.0f)
        getAccount(TO_ACCOUNT_ID).assertBalance(12345.0f)

        repeat(REPEAT_COUNT) {
            transferMoney(FROM_ACCOUNT_ID, TO_ACCOUNT_ID, 5.0f)
        }

        getAccount(FROM_ACCOUNT_ID).assertBalance(12300.0f)
        getAccount(TO_ACCOUNT_ID).assertBalance(12390.0f)

        getTransactions(FROM_ACCOUNT_ID)
        getTransactions(TO_ACCOUNT_ID)

    }

    private fun transferMoney(from: String, to: String, amount: Float) {
        given()
            .contentType(ContentType.JSON)
            .body(
                """{
                            "from": "$from",
                            "to": "$to",
                            "amount": $amount
                        }""".trimMargin()
            )
            .port(port)
            .`when`()
            .post("/transfer")
            .then()
            .statusCode(202)
    }

    private fun getAccount(accountId: String): ValidatableResponse {
        return given()
            .port(port)
            .`when`()
            .get("/accounts/$accountId")
            .then()
            .statusCode(200)
    }


    private fun getTransactions(accountId: String): ValidatableResponse {
        return given()
            .port(port)
            .`when`()
            .get("/accounts/$accountId/transactions")
            .then()
            .statusCode(200)
            .body("transactions.size()", Matchers.equalTo(REPEAT_COUNT))
    }
    private fun ValidatableResponse.assertBalance(amount: Float) {
        this.body("balance", Matchers.equalTo(amount))
    }
}

