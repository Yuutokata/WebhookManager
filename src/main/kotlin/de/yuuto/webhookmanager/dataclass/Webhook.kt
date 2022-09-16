package de.yuuto.webhookmanager.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class Webhook(
    val embeds: List<Embeds>
)

@Serializable
data class WebhookData(
    val webhooks: List<String>,
    val embeds: Webhook
)

@Serializable
data class Embeds(
    val title: String? = null,
    val description: String? = null,
    val color: Int? = null,
    val url: String? = null,
    val timestamp: String? = null,

    val thumbnail: ThumbnailData? = null,
    val image: Image? = null,

    val footer: Footer? = null,
    val author: Author? = null,

    val fields: List<Fields>? = null
)

@Serializable
data class ThumbnailData(
    val url: String
)

@Serializable
data class Fields(
    val name: String,
    val value: String,
    val inline: Boolean = false
)

@Serializable
data class Footer(
    val text: String? = null,
    val icon_url: String? = null
)

@Serializable
data class Image(
    val url: String? = null
)

@Serializable
data class Author(
    val name: String? = null,
    val url: String? = null
)