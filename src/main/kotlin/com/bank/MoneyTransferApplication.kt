package com.bank

import com.bank.base.configuration.ConfigManager
import org.slf4j.LoggerFactory

val logger = LoggerFactory.getLogger("com.bank.Main")

fun main() {
    logger.info("Starting Application")
    ConfigManager.init()
}
