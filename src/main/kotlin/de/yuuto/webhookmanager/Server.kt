package de.yuuto.webhookmanager

import de.yuuto.webhookmanager.dataclass.WebhookData
import de.yuuto.webhookmanager.util.Config
import de.yuuto.webhookmanager.util.Logger
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.coroutines.coroutineContext

class Server(private val queue: ConcurrentLinkedQueue<WebhookData>) {
    private val selectorManager = SelectorManager(Dispatchers.IO)

    private val serverSocket = aSocket(selectorManager).tcp().bind(Config.getAddress(), Config.getPort())

    suspend fun start() {
        Logger.info("de.yuuto.webhookmanager.Server is listening at ${serverSocket.localAddress}")

        while (coroutineContext.isActive) {
            val socket = serverSocket.accept()

            val receiveChannel = socket.openReadChannel()

            try {
                while (coroutineContext.isActive) {
                    val message = receiveChannel.readUTF8Line()

                    val data = Json.decodeFromString<WebhookData>(message.toString())

                    queue.add(data)
                }
            } catch (_: Throwable) {
                withContext(Dispatchers.IO) {
                    socket.close()
                }
            }
        }
    }
}

