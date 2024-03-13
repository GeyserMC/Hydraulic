@file:Suppress("UnstableApiUsage")

val modId = project.property("mod_id") as String

provided("org.jetbrains", "annotations")
provided("commons-io", "commons-io")

// TODO fix geyser-side: kyori is currently provided by Geyser
provided("net.kyori","adventure-text-serializer-gson")
provided("net.kyori", "adventure-text-serializer-json")
provided("net.kyori", "adventure-text-serializer-legacy")
provided("net.kyori", "adventure-api")

architectury {
    platformSetupLoomIde()
    neoForge()
}

val common: Configuration by configurations.creating
// Without this, the mixin config isn't read properly with the runServer neoforge task
val developmentNeoForge: Configuration = configurations.getByName("developmentNeoForge")
val includeTransitive: Configuration = configurations.getByName("includeTransitive")

configurations {
    compileClasspath.get().extendsFrom(configurations["common"])
    runtimeClasspath.get().extendsFrom(configurations["common"])
    developmentNeoForge.extendsFrom(configurations["common"])
}

dependencies {
    // See https://github.com/google/guava/issues/6618
    modules {
        module("com.google.guava:listenablefuture") {
            replacedBy("com.google.guava:guava", "listenablefuture is part of guava")
        }
    }

    common(project(":shared", configuration = "namedElements")) { isTransitive = false }
    neoForge(libs.neoforge)
    compileOnly(libs.geyser.api)

    shadow(project(path = ":shared", configuration = "transformProductionNeoForge")) { isTransitive = false }

    includeTransitive(libs.pack.converter)

    // TODO: properly add converter dependency to neoforge's runtime path
    // Currently, it causes about a dozen conflicts. Only needed for the runServer task
    forgeRuntimeLibrary(libs.pack.converter)
}

tasks {
    remapJar {
        dependsOn(shadowJar)
        inputFile.set(shadowJar.get().archiveFile)
        archiveBaseName.set("${modId}-neoforge")
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