plugins {
    `kotlin-dsl`
}

repositories {
    mavenLocal()

    gradlePluginPortal()

    maven("https://repo.opencollab.dev/maven-releases/")
    maven("https://repo.opencollab.dev/maven-snapshots/")

    maven("https://jitpack.io") {
        content {
            includeGroupByRegex("com\\.github\\..*")
        }
    }

    maven("https://maven.fabricmc.net/")
    maven("https://maven.neoforged.net/releases/")
    maven("https://maven.architectury.dev/")
}

dependencies {
    implementation(libs.indra)
    implementation(libs.shadow)
    implementation(libs.architectury.plugin)
    implementation(libs.architectury.loom)
}
