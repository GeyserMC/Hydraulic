plugins {
    id("hydraulic.publish-conventions")
    id("java-library")
    id("architectury-plugin")
    id("dev.architectury.loom-no-remap")
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
        archiveVersion.set(project.version.toString())
        archiveClassifier.set("shaded")
        mergeServiceFiles()
    }

    // This task combines the output of the "jar" task, which includes JiJ dependencies,
    // and the shadowJar for the final jar.
    // thanks bluemap
    // https://github.com/BlueMap-Minecraft/BlueMap/blob/cfe73115dc4d1bdd97bc659f41364da65a6a2179/implementations/fabric/build.gradle.kts#L93-L107
    register<Jar>("mergeShadowAndJarJar") {
        dependsOn( tasks.shadowJar, tasks.jar )
        // from sources / final name are configured in the respective projects
        archiveVersion.set("")
        archiveClassifier.set("")
    }

    build {
        dependsOn(tasks.getByName("mergeShadowAndJarJar"))
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
}