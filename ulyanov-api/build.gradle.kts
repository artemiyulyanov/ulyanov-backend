plugins {
    application
    kotlin("plugin.serialization")
    id("io.ktor.plugin")
}

dependencies {
    testImplementation(kotlin("test"))

    val ktorVersion: String by project
    val koinVersion: String by project
    val ktorStaticAuthVersion: String by project

    val bcryptVersion: String by project

    val exktVersion: String by project

    val logbackVersion: String by project
    val kmLogVersion: String by project

    val postgresqlVersion: String by project
    val ktormVersion: String by project
    val ktorServerLiquibaseVersion: String by project

    val viburVersion: String by project

    val dispatchVersion: String by project

    api(project(":ulyanov-common"))

    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-server-auth:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jwt:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-server-cors:$ktorVersion")
    implementation("io.ktor:ktor-server-status-pages:$ktorVersion")
    implementation("io.ktor:ktor-server-swagger:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("dev.d1s:ktor-static-authentication:$ktorStaticAuthVersion")

    implementation("dev.d1s.exkt:exkt-common:$exktVersion")
    implementation("dev.d1s.exkt:exkt-ktorm:$exktVersion")
    implementation("dev.d1s.exkt:exkt-konform:$exktVersion")
    implementation("dev.d1s.exkt:exkt-ktor-server:$exktVersion")
    implementation("dev.d1s.exkt:exkt-ktor-server-koin:$exktVersion")
    implementation("dev.d1s.exkt:exkt-ktor-server-postgres-support:$exktVersion")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.lighthousegames:logging:$kmLogVersion")

    implementation("org.postgresql:postgresql:$postgresqlVersion")
    implementation("org.ktorm:ktorm-core:$ktormVersion")
    implementation("dev.d1s:ktor-server-liquibase:$ktorServerLiquibaseVersion")
    implementation("org.ktorm:ktorm-support-postgresql:$ktormVersion")
    implementation("org.ktorm:ktorm-jackson:$ktormVersion")
    implementation("org.vibur:vibur-dbcp:$viburVersion")

    implementation("io.insert-koin:koin-core:$koinVersion")

    implementation("com.rickbusarow.dispatch:dispatch-core:$dispatchVersion")

    implementation("at.favre.lib:bcrypt:$bcryptVersion")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("my.artemyulyanov.api.MainKt")
}

ktor {
    docker {
        localImageName.set(project.name)
        jreVersion.set(JavaVersion.VERSION_21)
        jib.from.image = "eclipse-temurin:21-jre"
        jib.container.mainClass = "my.artemyulyanov.api.MainKt"
    }
}

tasks.register<Copy>("grabConfig") {
    from("../config/api.conf")
    into("src/main/resources")
}

tasks["processResources"].dependsOn(tasks["grabConfig"])

tasks.register<Delete>("cleanConfig") {
    delete("src/main/resources/api.conf")
}

tasks["clean"].dependsOn(tasks["cleanConfig"])

tasks.register<Copy>("grabDbChangelog") {
    from("../config/db/changelog.json")
    into("src/main/resources")
}

tasks["processResources"].dependsOn(tasks["grabDbChangelog"])

tasks.register<Delete>("cleanDbChangelog") {
    delete("src/main/resources/changelog.json")
}

tasks["clean"].dependsOn(tasks["cleanDbChangelog"])