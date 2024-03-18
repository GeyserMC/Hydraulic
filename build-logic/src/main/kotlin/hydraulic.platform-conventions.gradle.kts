plugins {
    id("hydraulic.publish-conventions")
    id("java-library")
    id("architectury-plugin")
    id("dev.architectury.loom")
}

// These are provided by Minecraft already, no need to include em
provided("com.google.code.gson", "gson")
provided("com.nukkitx.fastutil", "fastutil-common")
provided("com.nukkitx.fastutil", "fastutil-int-common")
provided("com.nukkitx.fastutil", "fastutil-int-object-maps")
provided("com.nukkitx.fastutil", "fastutil-int-sets")
provided("com.nukkitx.fastutil", "fastutil-object-common")
provided("com.nukkitx.fastutil", "fastutil-object-sets")

val minecraftVersion = project.property("minecraft_version") as String

architectury {
    minecraft = minecraftVersion
}

loom {
    silentMojangMappingsLicense()
}

configurations {
    create("includeTransitive").isTransitive = true
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

afterEvaluate {
    val providedDependenciesSet = getProvidedDependenciesForProject(project.name)
    configurations["includeTransitive"].resolvedConfiguration.resolvedArtifacts.forEach { dep ->
        if (!providedDependenciesSet.contains("${dep.moduleVersion.id.group}:${dep.moduleVersion.id.name}")) {
            println("Including dependency via JiJ: ${dep.moduleVersion.id}")
            dependencies.add("include", dep.moduleVersion.id.toString())
        } else {
            println("Not including ${dep.id} as it is already provided on the ${project.name} platform!")
        }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings(loom.officialMojangMappings())
}