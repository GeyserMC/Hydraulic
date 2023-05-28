val modId = project.property("mod_id") as String

val minecraftVersion = project.property("minecraft_version") as String
val forgeVersion = project.property("forge_version") as String

base.archivesName.set("${modId}-forge-${minecraftVersion}")

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
    api(project(":shared"))
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
        archiveBaseName.set("Hydraulic-Forge")
        archiveClassifier.set("")
        archiveVersion.set("")
    }
}