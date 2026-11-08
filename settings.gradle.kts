enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositories {
        // mavenLocal()
        mavenCentral()

        gradlePluginPortal()

        // Geyser, Floodgate, Cumulus etc.
        maven("https://repo.opencollab.dev/main/")

        // Minecraft
        maven("https://libraries.minecraft.net") {
            name = "minecraft"
            mavenContent {
                releasesOnly()
            }
        }

        // Architectury
        maven("https://maven.architectury.dev/")

        // NeoForge
        maven("https://maven.neoforged.net/releases/")

        // Fabric
        maven("https://maven.fabricmc.net/")

        // Sponge Snapshots
        maven("https://repo.spongepowered.org/repository/maven-public/")

        // Jitpack
        maven("https://jitpack.io/")
    }
}

pluginManagement {
    repositories {
        gradlePluginPortal()

        // Geyser, Floodgate, Cumulus etc.
        maven("https://repo.opencollab.dev/main/")

        // Architectury
        maven("https://maven.architectury.dev/")

        // NeoForge
        maven("https://maven.neoforged.net/releases")

        // Fabric
        maven("https://maven.fabricmc.net/")

        // Jitpack
        maven("https://jitpack.io/")
    }

    plugins {
        id("net.kyori.blossom") version "2.2.0"
        id("net.kyori.indra")
        id("net.kyori.indra.git")
        id("dev.opencollab.conduit") version "0.1.0-SNAPSHOT"
    }

    includeBuild("build-logic")
}

rootProject.name = "hydraulic-parent"

include(":shared")
include(":fabric")
include(":neoforge")

include(":test")
