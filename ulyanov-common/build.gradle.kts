plugins {
    kotlin("plugin.serialization")
}

dependencies {
    val kotlinxSerializationVersion: String by project

    val exktVersion: String by project

    val koinVersion: String by project

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")

    implementation("io.insert-koin:koin-ktor:$koinVersion")

    implementation("dev.d1s.exkt:exkt-konform:$exktVersion")
}

kotlin {
    jvmToolchain(17)
}