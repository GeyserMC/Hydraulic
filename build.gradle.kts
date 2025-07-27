plugins {
    id("hydraulic.build-logic")
}

val platforms = setOf(
    projects.fabric,
    projects.neoforge,
    projects.shared,
    projects.test
).map { it.dependencyProject }

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
//        mavenLocal()
        mavenCentral()

        maven("https://repo.opencollab.dev/main")
        maven("https://jitpack.io") {
            content {
                includeGroupByRegex("com\\.github\\..*")
            }
        }

        maven("https://maven.fabricmc.net/")
        maven("https://maven.neoforged.net/releases")
    }
}
