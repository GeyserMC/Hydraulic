plugins {
    `java-library`
    id("net.kyori.indra")
}

val minecraftVersion = project.property("minecraft_version") as String

dependencies {
    compileOnly("org.checkerframework:checker-qual:4.2.2")
}

indra {
    github("GeyserMC", "Hydraulic") {
        ci(true)
        issues(true)
        scm(true)
    }
    mitLicense()

    javaVersions {
        target(25)
    }
}

tasks {
    processResources {
        filesMatching(listOf("fabric.mod.json", "hydraulic.mixins.json", "META-INF/neoforge.mods.toml")) {
            expand(
                "id" to "hydraulic",
                "name" to "Hydraulic",
                "version" to project.version,
                "description" to project.description as String,
                "url" to "https://geysermc.org",
                "author" to "GeyserMC",
                "minecraft_version" to minecraftVersion
            )
        }
    }
}
