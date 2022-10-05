# WebhookManager

This is a socket webserver that can manage your Webhooks you need to send. It‘s also gonna sleep if you got rate limited.


## How to install

    git clone https://github.com/Yuutokata/WebhookManager.git

After you cloned it compile it by using Gradle shadowJar

## How to use in your Code

Create a Dataclass for your webhook in the webhookmanager and your project, it‘s json should look something like this

    {
    “webhooks“: [“url 1“, “url 2“],
    “embeds“: [data you want to send]
    }
    
Then copy the file in examples/sender.kt and add kto-network dependency.
