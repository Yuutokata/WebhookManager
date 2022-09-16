import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
}

group = "de.yuuto"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-client-core:2.1.0")
    implementation("io.ktor:ktor-client-cio:2.1.0")
    implementation("io.ktor:ktor-client-content-negotiation:2.1.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.1.0")
    implementation("io.ktor:ktor-server-websockets:2.1.0")
    implementation("io.ktor:ktor-network:2.1.0")
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")
    implementation("org.slf4j:slf4j-simple:2.0.1")
}


application {
    mainClass.set("de.yuuto.WebhookManager.MainKt")
}



tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "18"
}