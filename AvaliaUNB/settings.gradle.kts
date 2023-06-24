pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    plugins {
        kotlin("multiplatform").version(extra["kotlin.version"] as String)
        kotlin("kapt").version("1.8.22")
        id("org.jetbrains.compose").version(extra["compose.version"] as String)
    }
}

rootProject.name = "AvaliaUNB"

