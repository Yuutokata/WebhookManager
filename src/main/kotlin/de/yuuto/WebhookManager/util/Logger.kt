package de.yuuto.WebhookManager.util

import mu.KotlinLogging


object Logger {
    private val logger = KotlinLogging.logger {}

    fun info(text: String) {
        logger.info { text }
    }

    fun debug(text: String) {
        logger.debug { text }
    }

    fun error(text: String) {
        logger.error { text }
    }

    fun trace(text: String) {
        logger.trace { text }
    }

    fun warn(text: String) {
        logger.warn { text }
    }


}