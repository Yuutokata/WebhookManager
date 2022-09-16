package de.yuuto.WebhookManager

import de.yuuto.WebhookManager.dataclass.WebhookData
import de.yuuto.WebhookManager.util.Config
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentLinkedQueue


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