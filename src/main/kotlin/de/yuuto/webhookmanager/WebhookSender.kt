package de.yuuto.webhookmanager

import de.yuuto.webhookmanager.dataclass.WebhookData
import de.yuuto.webhookmanager.util.Logger
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.isActive
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.coroutines.coroutineContext

class WebhookSender(private val queue: ConcurrentLinkedQueue<WebhookData>) {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    private suspend fun sender(webhook: WebhookData) {
        while (coroutineContext.isActive) {
            webhook.webhooks.forEach {
                val response = client.post(it) {
                    contentType(ContentType.Application.Json)
                    setBody(webhook.embeds)
                }

                val rateLimitHeader = "x-rateLimit-remaining"

                if (response.status.isSuccess()) {
                    val code = response.status.hashCode()

                    Logger.info("Sent Webhook $it with Status Code $code")
                    return@forEach
                } else {
                    if (response.headers.contains(rateLimitHeader)) {
                        val remainingRequests = response.headers[rateLimitHeader]?.toInt()

                        if (remainingRequests == 0) {
                            val delay = response.headers[rateLimitHeader]?.toInt()

                            if (delay != null) {
                                Logger.warn("Sleeping for $delay")
                                delay(delay.seconds)
                                return@forEach
                            }
                        }
                    }
                    if (response.status.value == 429) {
                        val delay = response.headers[rateLimitHeader]?.toInt()
                        if (delay != null) {
                            Logger.warn("Sleeping for $delay")
                            delay(delay.seconds)
                            return@forEach
                        }
                    }
                }
            }

            break
        }
    }

    suspend fun thread() {
        Logger.info("Webhook Manager Thread started")

        val scope = CoroutineScope(Dispatchers.IO)

        while (coroutineContext.isActive) {
            for (i in 0 until queue.size) {
                val webhook = queue.elementAt(i)

                scope.launch {
                    sender(webhook = webhook)
                }

                queue.poll()
            }
        }

    }
}