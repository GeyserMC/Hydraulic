plugins {
    `java-library`
    id("net.kyori.indra")
}

val minecraftVersion = project.property("minecraft_version") as String

dependencies {
    compileOnly("org.checkerframework", "checker-qual", "3.19.0")
}

indra {
    github("GeyserMC", "Hydraulic") {
        ci(true)
        issues(true)
        scm(true)
    }
    mitLicense()

    javaVersions {
        target(17)
    }
}

tasks {
    processResources {
        // Spigot, BungeeCord, Velocity, Sponge, Fabric
        filesMatching(listOf("fabric.mod.json", "META-INF/mods.toml")) {
            expand(
                "id" to "hydraulic",
                "name" to "Hydraulic",
                "version" to project.version,
                "description" to project.description,
                "url" to "https://geysermc.org",
                "author" to "GeyserMC",
                "minecraft_version" to minecraftVersion
            )
        }
    }
}
