import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") apply false
}

allprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
    }

    val projectGroup: String by project
    val projectVersion: String by project

    group = projectGroup
    version = projectVersion

    repositories {
        mavenCentral()
        maven(url = "https://maven.d1s.dev/releases")
        maven(url = "https://maven.d1s.dev/snapshots")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    tasks.withType<KotlinCompile> {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }
    }
}