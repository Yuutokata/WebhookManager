import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object WebhookManager {
    private val selectorManager = SelectorManager(Dispatchers.IO)

    suspend fun send(data: YourDataClass) {

        val socket = aSocket(selectorManager).tcp().connect("127.0.0.1", 5001)

        val sendChannel = socket.openWriteChannel()

        val message = Json.encodeToString(data)

        sendChannel.writeStringUtf8(message)

        withContext(Dispatchers.IO) {
            socket.close()
        }

    }


}
