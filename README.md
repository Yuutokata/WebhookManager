
# Webhook Manager

## Description
This project is a simple webhook manager that allows you to send webhooks to multiple endpoints. It is written in Kotlin and uses Ktor as its web framework. I made it in the past to act like a message middle men for my sneaker monitor.

## Features
* Send webhooks to multiple endpoints
* Configure the webhook manager using a JSON file
* Log webhook requests and responses

## Requirements
* Java 18
* Gradle

## Configuration
The webhook manager is configured using a JSON file named `config.json`. The file must be located in the root directory of the project. The following is an example of a `config.json` file:

```json
{
  "address": "127.0.0.1",
  "port": 8080
}

```

The `address` field specifies the IP address that the webhook manager will listen on. The `port` field specifies the port that the webhook manager will listen on.

## Running the webhook manager

To run the webhook manager, you can use the following command:

```
gradle run

```

This will start the webhook manager and listen for webhook requests on the IP address and port specified in the `config.json` file.

## Sending webhooks

To send a webhook, you can use the following code:

Kotlin

```
import de.yuuto.webhookmanager.dataclass.WebhookData
import de.yuuto.webhookmanager.dataclass.Embeds
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object WebhookManager {
    private val selectorManager = SelectorManager(Dispatchers.IO)

    suspend fun send(data: WebhookData) {

        val socket = aSocket(selectorManager).tcp().connect("127.0.0.1", 5001)

        val sendChannel = socket.openWriteChannel()

        val message = Json.encodeToString(data)

        sendChannel.writeStringUtf8(message)

        withContext(Dispatchers.IO) {
            socket.close()
        }

    }


}

suspend fun main() {
    val embed = Embeds(title = "Hello World", description = "This is a test")
    val webhook = WebhookData(webhooks = listOf("your_webhook_url"), embeds = de.yuuto.webhookmanager.dataclass.Webhook(listOf(embed)))

    WebhookManager.send(webhook)
}

```

This code will send a webhook to the specified URL. The webhook will contain the following data:

JSON

```
{
  "embeds": [
    {
      "title": "Hello World",
      "description": "This is a test"
    }
  ]
}

```

## Logging

The webhook manager logs webhook requests and responses to the console. The logs include the following information:

-   The IP address of the client that sent the webhook request
-   The URL of the webhook
-   The status code of the webhook response
-   The body of the webhook response
