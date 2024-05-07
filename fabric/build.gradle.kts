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
    remapJar {
        dependsOn(shadowJar)
        inputFile.set(shadowJar.get().archiveFile)
        archiveBaseName.set("${modId}-fabric")
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

dependencies {
    modImplementation(libs.fabric.loader)
    modApi(libs.fabric.api)
    common(project(":shared", configuration = "namedElements")) { isTransitive = false }
    compileOnly(libs.geyser.api)

    shadow(project(path = ":shared", configuration = "transformProductionFabric")) {
        isTransitive = false
    }

    compileOnly(libs.asm)

    modRuntimeOnly(libs.pack.converter)
    includeTransitive(libs.pack.converter)

    modLocalRuntime(libs.geyser.fabric) {
        exclude(group = "io.netty")
        exclude(group = "io.netty.incubator")
    }
}

sourceSets {
    main {
        resources {
            srcDirs(project(":shared").sourceSets["main"].resources.srcDirs)
        }
    }
}