@file:Suppress("UnstableApiUsage")

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("dev.opencollab.conduit")
}

val modId = project.property("mod_id") as String

provided("org.jetbrains", "annotations")
provided("commons-io", "commons-io")
provided("com.google.errorprone", "error_prone_annotations")

architectury {
    platformSetupLoomIde()
    neoForge()
}

patchGeyserRuntimeArtifacts("neoforge", bundleClasses = true)

val common: Configuration = configurations.create("common")
// Without this, the mixin config isn't read properly with the runServer neoforge task
val developmentNeoForge: Configuration = configurations.getByName("developmentNeoForge")
val includeTransitive: Configuration = configurations.getByName("includeTransitive")
val packConverter: Configuration = configurations.create("packConverter")

configurations {
    compileClasspath.get().extendsFrom(configurations["common"])
    runtimeClasspath.get().extendsFrom(configurations["common"])
    developmentNeoForge.extendsFrom(configurations["common"])
    shadow.get().extendsFrom(packConverter)
}

// NeoForge only applies mixins to the game layer, which takes classpath jars marked as game libraries
val packConverterLibrary = tasks.register<ShadowJar>("packConverterLibrary") {
    archiveClassifier.set("pack-converter")
    configurations = listOf(packConverter)
    mergeServiceFiles()
    exclude("module-info.class")
    manifest.attributes("FMLModType" to "GAMELIBRARY")
}

dependencies {
    // See https://github.com/google/guava/issues/6618
    modules {
        module("com.google.guava:listenablefuture") {
            replacedBy("com.google.guava:guava", "listenablefuture is part of guava")
        }
    }

    common(project(":shared")) { isTransitive = false }
    neoForge(libs.neoforge)
    compileOnly(libs.geyser.api)

    shadow(project(path = ":shared", configuration = "transformProductionNeoForge")) { isTransitive = false }

    packConverter(libs.pack.converter) {
        exclude(group = "net.kyori", module = "adventure-api")
        exclude(group = "net.kyori", module = "adventure-key")
        exclude(group = "net.kyori", module = "adventure-text-serializer-gson")
        exclude(group = "net.kyori", module = "adventure-text-serializer-json")
        exclude(group = "net.kyori", module = "adventure-text-serializer-legacy")
        exclude(group = "com.google.code.gson")
        exclude(group = "commons-io")
        exclude(group = "org.jetbrains")
        exclude(group = "com.nukkitx.fastutil")
    }
    localRuntime(files(packConverterLibrary))

    runtimeOnly(libs.examination.api)
    runtimeOnly(libs.examination.string)
    includeTransitive(libs.examination.api)
    includeTransitive(libs.examination.string)

    localRuntime(libs.bundles.configurate)
    shadow(libs.bundles.configurate) { isTransitive = false }

    localRuntime(libs.geyser.neoforge) { isTransitive = false }
}

tasks {
    named<Jar>("mergeShadowAndJarJar") {
        from (
            zipTree( shadowJar.map { it.outputs.files.singleFile } ).matching {
                exclude("LICENSE")
            },
            zipTree( jar.map { it.outputs.files.singleFile } ).matching {
                include("META-INF/jars/**")
                include("META-INF/jarjar/**")
                include("LICENSE")
            }
        )
        archiveBaseName.set("${modId}-neoforge")
    }

    shadowJar {
        archiveClassifier.set("dev-shadow")
        relocate("org.spongepowered.configurate", "org.geysermc.hydraulic.shaded.org.spongepowered.configurate")
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