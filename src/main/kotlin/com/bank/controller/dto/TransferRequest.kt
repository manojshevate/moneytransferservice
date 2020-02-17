package com.bank.controller.dto

data class TransferRequest(val from: String,
                           val to: String,
                           val amount: Double)
