@file:Suppress("UnstableApiUsage")

val modId = project.property("mod_id") as String

provided("org.jetbrains", "annotations")
provided("commons-io", "commons-io")

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

    // TODO fix neoforge runServer task
    modRuntimeOnly(libs.pack.converter)
    includeTransitive(libs.pack.converter)
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

sourceSets {
    main {
        resources {
            srcDirs(project(":shared").sourceSets["main"].resources.srcDirs)
        }
    }
}