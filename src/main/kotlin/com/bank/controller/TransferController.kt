package com.bank.controller

import com.bank.base.exception.AccountNotFoundException
import com.bank.base.exception.InsufficientFundsException
import com.bank.command.handler.TransferMoneyCommandHandler
import com.bank.controller.model.ErrorResponse
import com.bank.controller.model.TransferRequest
import com.bank.model.command.TransferMoneyCommand
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import spark.Response
import spark.Spark.exception
import spark.Spark.post
import javax.inject.Inject

class TransferController {

    @Inject
    lateinit var commandHandler: TransferMoneyCommandHandler

    companion object {
        private val objectMapper = jacksonObjectMapper()
    }

    init {
        post("/transfer") { req, res ->
            val transferRequest = objectMapper.readValue(req.body(), TransferRequest::class.java)

            val command = TransferMoneyCommand(
                transferRequest.from,
                transferRequest.to,
                transferRequest.amount
            )

            commandHandler.handle(command)

            res.run {
                status(204)
            }
        }

        exception(AccountNotFoundException::class.java) { ex, _, res ->
            returnBadRequest(res, ex.message!!)
        }

        exception(InsufficientFundsException::class.java) { ex, _, res ->
            returnBadRequest(res, ex.message!!)
        }
    }

    private fun returnBadRequest(res: Response, errorMessage: String) {
        res.run {
            status(400)
            res.type("application/json")
            body(
                objectMapper.writeValueAsString(ErrorResponse("bad_request", errorMessage))
            )
        }
    }
}