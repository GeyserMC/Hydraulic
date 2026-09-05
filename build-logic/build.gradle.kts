plugins {
    `kotlin-dsl`
}

repositories {
    // mavenLocal()

    gradlePluginPortal()

    // Geyser, Floodgate, Cumulus etc.
    maven("https://repo.opencollab.dev/maven-releases/")
    maven("https://repo.opencollab.dev/maven-snapshots/")

    // Fabric
    maven("https://maven.fabricmc.net/")

    // NeoForge
    maven("https://maven.neoforged.net/releases/")

    // Architectury
    maven("https://maven.architectury.dev/")
}

dependencies {
    implementation(libs.indra)
    implementation(libs.shadow)
    implementation(libs.architectury.plugin)
    implementation(libs.architectury.loom)
}
