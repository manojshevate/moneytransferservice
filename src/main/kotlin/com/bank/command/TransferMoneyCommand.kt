package com.bank.model.command

data class TransferMoneyCommand(val from: String,
                                val to: String,
                                val amount: Double)

