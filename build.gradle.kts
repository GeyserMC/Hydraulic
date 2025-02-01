plugins {
    id("hydraulic.build-logic")
}

val platforms = setOf(
    projects.fabric,
    projects.neoforge,
    projects.shared
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
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }
}