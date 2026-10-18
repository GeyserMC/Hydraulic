import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.withGroovyBuilder

val providedDependencies = mutableMapOf<String, MutableSet<String>>()

fun Project.provided(pattern: String, name: String, excludedOn: Int = 0b110) {
    providedDependencies.getOrPut(project.name) { mutableSetOf() }
        .add("${calcExclusion(pattern, 0b100, excludedOn)}:${calcExclusion(name, 0b10, excludedOn)}")
}

fun Project.provided(dependency: ProjectDependency) =
    provided(dependency.group!!, dependency.name)

fun Project.provided(dependency: MinimalExternalModuleDependency) =
    provided(dependency.module.group, dependency.module.name)

fun Project.provided(provider: Provider<MinimalExternalModuleDependency>) =
    provided(provider.get())

fun getProvidedDependenciesForProject(projectName: String): Set<String> {
    return providedDependencies.getOrDefault(projectName, emptySet()).toSet()
}

private fun calcExclusion(section: String, bit: Int, excludedOn: Int): String =
    if (excludedOn and bit > 0) section else ""

fun Project.patchGeyserRuntimeArtifacts(platform: String, bundleClasses: Boolean = false) {
    val patch = tasks.register("patchGeyserRuntimeArtifacts") {
        doLast {
            val platformArtifact = configurations.getByName("localRuntime")
                .resolvedConfiguration
                .resolvedArtifacts
                .first { it.moduleVersion.id.group == "org.geysermc.geyser" && it.name == platform }
            val version = platformArtifact.moduleVersion.id.version
            val geyserJars = configurations.detachedConfiguration(
                dependencies.create("org.geysermc.geyser:core:$version"),
                dependencies.create("org.geysermc.geyser:mod:$version"),
                dependencies.create("org.geysermc.geyser:api:$version")
            ).apply { isTransitive = false }.resolvedConfiguration.resolvedArtifacts.associate { it.name to it.file }

            ant.withGroovyBuilder {
                "zip"("destfile" to platformArtifact.file.absolutePath, "update" to "true") {
                    if (bundleClasses) {
                        // Loaders that keep classpath libraries out of the game layer need Geyser in one jar, as it ships.
                        for (jar in geyserJars.values) {
                            "zipfileset"("src" to jar.absolutePath, "excludes" to "META-INF/**,LICENSE,git.properties")
                        }
                    } else {
                        // Geyser's mod platforms resolve resources from their own mod jar only, so copy
                        // every non-class resource out of core instead of an ever-growing include list.
                        "zipfileset"("src" to geyserJars.getValue("core").absolutePath, "excludes" to "org/**,META-INF/**,LICENSE,git.properties")
                        "zipfileset"("src" to geyserJars.getValue("mod").absolutePath, "includes" to "geyser.mixins.json")
                    }
                }
            }
        }
    }

    tasks.named("runServer") {
        dependsOn(patch)
    }
}
