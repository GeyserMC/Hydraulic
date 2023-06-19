val modId = project.property("mod_id") as String

val minecraftVersion = project.property("minecraft_version") as String
val forgeVersion = project.property("forge_version") as String

architectury {
    platformSetupLoomIde()
    forge()
}

loom {
    forge {
        mixinConfig("hydraulic.mixins.json")
    }
}

dependencies {
    forge("net.minecraftforge:forge:${minecraftVersion}-${forgeVersion}")
    api(project(path = ":shared", configuration = "namedElements"))
    shadow(project(path = ":shared", configuration = "transformProductionForge")) {
        isTransitive = false
    }

    compileOnly(libs.geyser.api)
    compileOnly(libs.geyser.core) {
        exclude(group = "io.netty")
    }
}

tasks {
    remapJar {
        dependsOn(shadowJar)
        inputFile.set(shadowJar.get().archiveFile)
        archiveBaseName.set("${modId}-forge-${minecraftVersion}")
        archiveClassifier.set("")
        archiveVersion.set("")
    }

    shadowJar {
        archiveClassifier.set("dev-shadow")
    }

    jar {
        archiveClassifier.set("dev")
    }
}