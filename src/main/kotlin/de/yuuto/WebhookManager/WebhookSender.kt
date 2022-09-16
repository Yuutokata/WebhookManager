package de.yuuto.WebhookManager

import de.yuuto.WebhookManager.dataclass.WebhookData
import de.yuuto.WebhookManager.util.Logger
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.isActive
import org.slf4j.event.LoggingEvent
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.coroutines.coroutineContext

class WebhookSender(private val queue: ConcurrentLinkedQueue<WebhookData>) {

    private val client = HttpClient() {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun sender(webhook: WebhookData) {
        while (coroutineContext.isActive) {

            for (data in webhook.webhooks) {
                val response: HttpResponse = client.post(data) {
                    contentType(ContentType.Application.Json)
                    setBody(webhook.embeds)
                }

                if (response.status.isSuccess()) {
                    val code: Int = response.status.hashCode()

                    Logger.info("Sended Webhook $data with Status Code $code")
                    continue
                } else {

                    if (response.headers.contains("x-rateLimit-remaining")) {
                        val remainingRequests: Int? = response.headers["x-rateLimit-remaining"]?.toInt()

                        if (remainingRequests == 0) {
                            val delay: Int? = response.headers["x-rateLimit-reset-after"]?.toInt()

                            if (delay != null) {
                                Logger.warn("Sleeping for $delay")
                                delay(delay.seconds)
                                continue
                            }
                        }
                    }
                    if (response.status.value == 429) {
                        val delay: Int? = response.headers["x-rateLimit-reset-after"]?.toInt()
                        if (delay != null) {

                            Logger.warn("Sleeping for $delay")
                            delay(delay.seconds)

                            continue
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
                val webhook: WebhookData = queue.elementAt(i)

                scope.launch {
                    sender(webhook = webhook)
                }

                queue.poll()
            }
        }

    }
}