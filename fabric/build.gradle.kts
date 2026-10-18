val modId = project.property("mod_id") as String

architectury {
    platformSetupLoomIde()
    fabric()
}

val common: Configuration by configurations.creating
val developmentFabric: Configuration = configurations.getByName("developmentFabric")
val includeTransitive: Configuration = configurations.getByName("includeTransitive")

configurations {
    compileClasspath.get().extendsFrom(configurations["common"])
    runtimeClasspath.get().extendsFrom(configurations["common"])
    developmentFabric.extendsFrom(configurations["common"])
}

tasks {
    named<Jar>("mergeShadowAndJarJar") {
        from (
            zipTree( shadowJar.map { it.outputs.files.singleFile } ).matching {
                exclude("fabric.mod.json")
                exclude("LICENSE")
            },
            zipTree( jar.map { it.outputs.files.singleFile } ).matching {
                include("META-INF/jars/**")
                include("fabric.mod.json")
                include("LICENSE")
            }
        )
        archiveBaseName.set("${modId}-fabric")
    }

    register("patchGeyserRuntimeArtifacts") {
        doLast {
            val runtimeArtifacts = configurations.getByName("localRuntime")
                .resolvedConfiguration
                .resolvedArtifacts
            val fabricJar = runtimeArtifacts.first {
                it.moduleVersion.id.group == "org.geysermc.geyser" && it.name == "fabric"
            }.file
            val coreJar = runtimeArtifacts.first {
                it.moduleVersion.id.group == "org.geysermc.geyser" && it.name == "core"
            }.file
            val modJar = runtimeArtifacts.first {
                it.moduleVersion.id.group == "org.geysermc.geyser" && it.name == "mod"
            }.file

            ant.withGroovyBuilder {
                "zip"("destfile" to fabricJar.absolutePath, "update" to "true") {
                    // Geyser's fabric platform resolves resources from its own mod jar only, so copy
                    // every non-class resource out of core instead of an ever-growing include list.
                    "zipfileset"("src" to coreJar.absolutePath, "excludes" to "org/**,META-INF/**,LICENSE,git.properties")
                    "zipfileset"("src" to modJar.absolutePath, "includes" to "geyser.mixins.json")
                }
            }
        }
    }

    named("runServer") {
        dependsOn("patchGeyserRuntimeArtifacts")
    }

    shadowJar {
        archiveClassifier.set("dev-shadow")
        relocate("org.spongepowered.configurate", "org.geysermc.hydraulic.shaded.org.spongepowered.configurate")
    }

    jar {
        archiveClassifier.set("dev")
    }
}

dependencies {
    implementation(libs.fabric.loader)
    api(libs.fabric.api)
    common(project(":shared")) { isTransitive = false }
    compileOnly(libs.geyser.api)

    shadow(project(path = ":shared", configuration = "transformProductionFabric")) {
        isTransitive = false
    }

    runtimeOnly(libs.pack.converter)
    runtimeOnly(libs.examination.api)
    runtimeOnly(libs.examination.string)
    includeTransitive(libs.pack.converter)

    localRuntime(libs.bundles.configurate)
    shadow(libs.bundles.configurate) { isTransitive = false }

    localRuntime(libs.geyser.fabric) {
        exclude(group = "io.netty")
        exclude(group = "io.netty.incubator")
        exclude(group = "org.incendo")
    }
    localRuntime(libs.geyser.core) {
        exclude(group = "io.netty")
        exclude(group = "io.netty.incubator")
    }

    localRuntime(project(":test"))
}

sourceSets {
    main {
        resources {
            srcDirs(project(":shared").sourceSets["main"].resources.srcDirs)
        }
    }
}
