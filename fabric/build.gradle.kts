import org.gradle.api.tasks.SourceSetContainer

val modId = project.property("mod_id") as String
val localPackConverterProject = rootProject.findProject(":converter")

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
        relocate("org.cloudburstmc", "org.geysermc.geyser.shaded.org.cloudburstmc")

        localPackConverterProject?.let { converterProject ->
            dependsOn(converterProject.tasks.named("classes"))
            from(converterProject.extensions.getByType<SourceSetContainer>()["main"].output)
        }
    }

    jar {
        archiveClassifier.set("dev")
    }

    register("printFabricClasspath") {
        group = "verification"
        description = "Prints Fabric classpath configurations used for converter packaging diagnostics."

        doLast {
            listOf("compileClasspath", "runtimeClasspath", "shadow", "includeTransitive").forEach { configurationName ->
                val configuration = configurations.findByName(configurationName) ?: return@forEach
                println("[$path] $configurationName")
                configuration.files.sortedBy { it.absolutePath }.forEach { file ->
                    println("  ${file.absolutePath}")
                }
            }
        }
    }

    named("shadowJar") {
        dependsOn("printFabricClasspath")
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

    compileOnly(libs.asm)

    if (localPackConverterProject != null) {
        implementation(project(":converter"))
        shadow(project(":converter")) {
            isTransitive = true
        }
    } else {
        runtimeOnly(libs.pack.converter)
        includeTransitive(libs.pack.converter)
    }

    localRuntime(libs.geyser.fabric) {
        exclude(group = "io.netty")
        exclude(group = "io.netty.incubator")
        exclude(group = "org.incendo")
    }
}

sourceSets {
    main {
        resources {
            srcDirs(project(":shared").sourceSets["main"].resources.srcDirs)
        }
    }
}
