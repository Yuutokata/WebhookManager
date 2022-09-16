package de.yuuto.webhookmanager

import de.yuuto.webhookmanager.dataclass.WebhookData
import de.yuuto.webhookmanager.util.Config
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.time.Duration.Companion.seconds

suspend fun main() {
    Config()
    val scope = CoroutineScope(Dispatchers.IO)

    val queue = ConcurrentLinkedQueue<WebhookData>()

    scope.launch {
        Server(queue).start()
    }

    scope.launch {
        WebhookSender(queue).thread()
    }

    coroutineScope {
        launch {
            while (isActive) {
                fun foo() {}
            }
        }
    }
}