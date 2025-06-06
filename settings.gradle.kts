rootProject.name = "ulyanov-backend"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings

        val ktorVersion: String by settings

        kotlin("jvm") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion

        id("io.ktor.plugin") version ktorVersion

        id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

include("ulyanov-api")
include("ulyanov-common")
