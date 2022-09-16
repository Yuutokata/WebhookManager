package de.yuuto.webhookmanager.util

import de.yuuto.webhookmanager.dataclass.ConfigData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.readText

object Config {
    private lateinit var configFile: Path
    private lateinit var configData: ConfigData

    operator fun invoke() {
        configFile = Path("./config.json")
        configData = Json.decodeFromString(configFile.readText())
    }

    fun getAddress() = configData.address

    fun getPort() = configData.port

}