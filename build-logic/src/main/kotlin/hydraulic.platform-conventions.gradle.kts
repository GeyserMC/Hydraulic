import org.gradle.kotlin.dsl.dependencies

plugins {
    id("hydraulic.publish-conventions")
    id("architectury-plugin")
    id("dev.architectury.loom")
}

val minecraftVersion = project.property("minecraft_version") as String

architectury {
    platformSetupLoomIde()

    minecraft = minecraftVersion
}

loom {
    silentMojangMappingsLicense()
}

tasks {
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this task, sources will not be generated.
    sourcesJar {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
    }

    shadowJar {
        // Mirrors the example fabric project, otherwise tons of dependencies are shaded that shouldn't be
        configurations = listOf(project.configurations.shadow.get())
        // The remapped shadowJar is the final desired mod jar
        archiveVersion.set(project.version.toString())
        archiveClassifier.set("shaded")
    }
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings(loom.officialMojangMappings())
}