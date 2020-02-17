package com.bank.controller

import com.bank.base.exception.AccountNotFoundException
import com.bank.controller.dto.TransactionResponse
import com.bank.services.AccountService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import spark.Response
import spark.Spark.exception
import spark.Spark.get
import javax.inject.Inject

class AccountController {

    @Inject
    lateinit var accountService: AccountService

    companion object {
        private val objectMapper = jacksonObjectMapper()
    }

    init {
        get("/accounts/:id") { req, res ->
            val accountId = req.params("id")
            val account = accountService.getAccount(accountId)

            res.type("application/json")
            objectMapper.writeValueAsString(account)
        }

        get("/accounts/:id/transactions") { req, res ->
            val accountId = req.params("id")
            val transactions = accountService.getTransactionsByAccountId(accountId)

            res.type("application/json")
            objectMapper.writeValueAsString(TransactionResponse(transactions))
        }


        exception(AccountNotFoundException::class.java) { _, _, res ->
            returnNotFound(res)
        }
    }

    private fun returnNotFound(res: Response) {
        res.run {
            status(404)
        }
    }
}