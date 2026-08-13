plugins {
    id("hydraulic.build-logic")
}

val platforms = setOf(
    projects.fabric,
    projects.neoforge,
    projects.shared,
    projects.test
).map { it -> project.project(it.path) }

subprojects {
    when (this) {
        in platforms -> plugins.apply("hydraulic.platform-conventions")
        else -> plugins.apply("hydraulic.base-conventions")
    }
}

allprojects {
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    repositories {
        // mavenLocal()
        mavenCentral()

        // Geyser, Floodgate, Cumulus etc.
        maven("https://repo.opencollab.dev/main")

        // Fabric
        maven("https://maven.fabricmc.net/")

        // NeoForge
        maven("https://maven.neoforged.net/releases")

        // creative
        maven("https://repo.nexomc.com/releases/")

        // Modrinth
        exclusiveContent {
            forRepository {
                maven {
                    name = "Modrinth"
                    url = uri("https://api.modrinth.com/maven")
                }
            }
            filter {
                includeGroup("maven.modrinth")
            }
        }
    }
}
