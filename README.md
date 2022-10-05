# WebhookManager

This is a socket webserver that can manage your Webhooks you need to send. It‘s also gonna sleep if you got rate limited.
Its not really useful because it takes a lot of performance but im gonna fix that in the future.

## How to install

    git clone https://github.com/Yuutokata/WebhookManager.git

After you cloned it compile it by using Gradle shadowJar

## How to use in your Code

Create a Dataclass for your webhook in the webhookmanager and your project, it‘s json should look something like this

    {
    “webhooks“: [“url 1“, “url 2“],
    “embeds“: [data you want to send]
    }
    
Then copy the file in examples/sender.kt and add ktor-network dependency.

The dataclass Webhook that is already in the Manager is for discord embeds.
