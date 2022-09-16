package de.yuuto.WebhookManager.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class ConfigData(
    val address: String,
    val port: Int
)