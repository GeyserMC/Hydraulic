import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.provider.Provider

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