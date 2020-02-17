package com.bank.controller

import com.bank.base.exception.AccountNotFoundException
import com.bank.base.exception.InsufficientFundsException
import com.bank.base.exception.InvalidRequestException
import com.bank.command.TransferMoneyCommand
import com.bank.command.handler.TransferMoneyCommandHandler
import com.bank.controller.dto.ErrorResponse
import com.bank.controller.dto.TransferRequest
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import spark.Request
import spark.Response
import spark.Spark
import spark.Spark.exception
import spark.Spark.post
import javax.inject.Inject

class TransferController {

    @Inject
    lateinit var commandHandler: TransferMoneyCommandHandler

    companion object {
        private val objectMapper = jacksonObjectMapper()

        private const val INVALID_JSON = "Invalid request object"
        private const val INVALID_TRANSFER_AMOUNT = "Invalid transfer amount"
        private const val SENDER_RECIPIENT_SAME = "Can not send money to your own account"


        private val CLIENT_EXCEPTIONS = listOf(
            AccountNotFoundException::class.java,
            InvalidRequestException::class.java,
            InsufficientFundsException::class.java
        )
    }

    init {
        post("/transfer") { req, res ->

            val request = parseAndValidateRequest(req)

            val command = TransferMoneyCommand(
                from = request.from,
                to = request.to,
                amount = request.amount
            )

            val response = commandHandler.handle(command)

            res.run {
                status(202)
                res.type("application/json")
                objectMapper.writeValueAsString(response)
            }
        }

        CLIENT_EXCEPTIONS
            .forEach {
                exception(it) { ex, _, res ->
                    returnBadRequest(res, ex.message!!)
                }
            }
    }

    private fun parseAndValidateRequest(req: Request): TransferRequest {
        val transferRequest = try {
            objectMapper.readValue(req.body(), TransferRequest::class.java)
        } catch (ex: JsonProcessingException) {
            throw InvalidRequestException(INVALID_JSON)
        }

        if (transferRequest.from == transferRequest.to) {
            throw InvalidRequestException(SENDER_RECIPIENT_SAME)
        }
        if (transferRequest.amount <= 0.0) {
            throw InvalidRequestException(INVALID_TRANSFER_AMOUNT)
        }
        return transferRequest
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