package com.bank.controller.model

data class TransferRequest(val from: String,
                           val to: String,
                           val amount: Double)
