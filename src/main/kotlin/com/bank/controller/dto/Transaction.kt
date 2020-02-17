package com.bank.controller.dto

data class Transaction(val id: String = "",
                       val fromAccount: String = "",
                       val toAccount: String = "",
                       val amount: Double = 0.0,
                       val type: Type,
                       val createdDate: String)

enum class Type {
    CREDIT,
    DEBIT
}