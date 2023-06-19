val modId = project.property("mod_id") as String
val minecraftVersion = project.property("minecraft_version") as String

val fabricVersion = project.property("fabric_version") as String
val fabricLoaderVersion = project.property("fabric_loader_version") as String

architectury {
    platformSetupLoomIde()
    fabric()
}

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${fabricLoaderVersion}")
    modApi("net.fabricmc.fabric-api:fabric-api:${fabricVersion}")
    api(project(path = ":shared", configuration = "namedElements"))
    shadow(project(path = ":shared", configuration = "transformProductionFabric")) {
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
        archiveBaseName.set("${modId}-fabric-${minecraftVersion}")
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