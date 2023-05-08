plugins {
    id("org.spongepowered.gradle.vanilla") version("0.2.1-SNAPSHOT")
}

val modId = project.property("mod_id") as String
val minecraftVersion = project.property("minecraft_version") as String

base.archivesName.set("${modId}-shared-${minecraftVersion}")

minecraft {
    version("1.19.4")
    runs {
        val commonRunsEnabled = project.findProperty("common_runs_enabled")?.toString()?.toBoolean() ?: true

        if (commonRunsEnabled) {
            server(project.findProperty("common_server_run_name")?.toString() ?: "vanilla_server") {
                workingDirectory(file("run"))
            }
            client(project.findProperty("common_client_run_name")?.toString() ?: "vanilla_client") {
                workingDirectory(file("run"))
            }
        }
    }
}

dependencies {
    compileOnly(libs.mixin)
    compileOnly(libs.geyser.api)
    compileOnly(libs.geyser.core) {
        exclude(group = "io.netty")
    }

    implementation(project(":bedrock-pack-schema"))
    implementation(libs.auto.service)
    annotationProcessor(libs.auto.service)
}