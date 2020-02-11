package com.bank.controller

import com.bank.controller.model.ErrorResponse
import com.bank.exceptions.AccountNotFoundException
import com.bank.services.AccountService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import spark.Response
import spark.Spark
import spark.Spark.*
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