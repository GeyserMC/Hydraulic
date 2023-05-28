import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.named

fun Project.relocate(pattern: String) {
    tasks.named<ShadowJar>("shadowJar") {
        relocate(pattern, "org.geysermc.hydraulic.shaded.$pattern")
    }
}

fun Project.exclude(group: String) {
    tasks.named<ShadowJar>("shadowJar") {
        exclude(group)
    }
}

val providedDependencies = mutableMapOf<String, MutableSet<String>>()

fun Project.provided(pattern: String, name: String, version: String, excludedOn: Int = 0b110) {
    providedDependencies.getOrPut(project.name) { mutableSetOf() }
        .add("${calcExclusion(pattern, 0b100, excludedOn)}:" +
                "${calcExclusion(name, 0b10, excludedOn)}:" +
                calcExclusion(version, 0b1, excludedOn))
    dependencies.add("compileOnlyApi", "$pattern:$name:$version")
}

fun Project.provided(dependency: ProjectDependency) =
    provided(dependency.group!!, dependency.name, dependency.version!!)

fun Project.provided(dependency: MinimalExternalModuleDependency) =
    provided(dependency.module.group, dependency.module.name, dependency.versionConstraint.requiredVersion)

fun Project.provided(provider: Provider<MinimalExternalModuleDependency>) =
    provided(provider.get())

private fun calcExclusion(section: String, bit: Int, excludedOn: Int): String =
    if (excludedOn and bit > 0) section else ""