plugins {
    id("java")
    id("java-library")
}

val description = project.property("description") as String

val minecraftVersion = project.property("minecraft_version") as String

val modId = project.property("mod_id") as String
val modName = project.property("mod_name") as String
val modAuthor = project.property("mod_author") as String

allprojects {
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    tasks.withType<ProcessResources> {
        filesMatching(listOf("META-INF/mods.toml", "fabric.mod.json")) {
            expand(mapOf(
                "mod_id" to modId,
                "mod_name" to modName,
                "mod_author" to modAuthor,

                "minecraft_version" to minecraftVersion,
                "version" to project.version,
                "description" to description
            ))
        }
    }

    repositories {
        mavenCentral()

        maven("https://repo.opencollab.dev/main")
        maven("https://jitpack.io") {
            content {
                includeGroupByRegex("com\\.github\\..*")
            }
        }

        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }
}

subprojects {
    apply {
        plugin("java-library")
    }
}