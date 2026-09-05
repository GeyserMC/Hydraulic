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

    localRuntime(project(":test"))
}

sourceSets {
    main {
        resources {
            srcDirs(project(":shared").sourceSets["main"].resources.srcDirs)
        }
    }
}
