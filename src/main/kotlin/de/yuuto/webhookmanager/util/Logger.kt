package de.yuuto.webhookmanager.util

import mu.KotlinLogging


object Logger {
    private val logger = KotlinLogging.logger {}

    fun info(text: String) {
        logger.info { text }
    }

    fun warn(text: String) {
        logger.warn { text }
    }


}