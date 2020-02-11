package com.bank.helper

import spark.Spark

fun startServer(routeInit: () -> Unit): Int {
    Thread.sleep(500)
    Spark.port(0)
    routeInit()
    Spark.awaitInitialization()
    return Spark.port()
}
