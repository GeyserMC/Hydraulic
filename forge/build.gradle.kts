import net.minecraftforge.gradle.common.util.RunConfig

plugins {
    id("eclipse")
    id("net.minecraftforge.gradle") version("5.1.+")
}

val modId = project.property("mod_id") as String
val modName = project.property("mod_name") as String
val modAuthor = project.property("mod_author") as String
val description = project.property("description") as String

val minecraftVersion = project.property("minecraft_version") as String
val forgeVersion = project.property("forge_version") as String

base.archivesName.set("${modId}-forge-${minecraftVersion}")

minecraft {
    mappings("official", minecraftVersion)

    if (project.hasProperty("forge_ats_enabled") && project.findProperty("forge_ats_enabled").toString().toBoolean()) {
        // This location is hardcoded in Forge and can not be changed.
        // https://github.com/MinecraftForge/MinecraftForge/blob/be1698bb1554f9c8fa2f58e32b9ab70bc4385e60/fmlloader/src/main/java/net/minecraftforge/fml/loading/moddiscovery/ModFile.java#L123
        accessTransformer(file("src/main/resources/META-INF/accesstransformer.cfg"))
        project.logger.debug("Forge Access Transformers are enabled for this project.")
    }

    runs(closureOf<NamedDomainObjectContainer<RunConfig>> {
        create("client") {
            workingDirectory(project.file("run"))
            ideaModule = "${rootProject.name}.${project.name}.main"
            property("mixin.env.remapRefMap", "true")
            property("mixin.env.refMapRemappingFile", "${projectDir}/build/createSrgToMcp/output.srg")
            mods.create(modId).sources(sourceSets["main"], project(":shared").sourceSets["main"])
        }

        create("server") {
            workingDirectory(project.file("run"))
            ideaModule = "${rootProject.name}.${project.name}.main"
            property("mixin.env.remapRefMap", "true")
            property("mixin.env.refMapRemappingFile", "${projectDir}/build/createSrgToMcp/output.srg")
            mods.create(modId).sources(sourceSets["main"], project(":shared").sourceSets["main"])
        }

        create("data") {
            workingDirectory(project.file("run"))
            ideaModule = "${rootProject.name}.${project.name}.main"
            args("--mod", modId,
                "--all",
                "--output", file("src/generated/resources/").toString(),
                "--existing", file("src/main/resources/").toString())
            property("mixin.env.remapRefMap", "true")
            property("mixin.env.refMapRemappingFile", "${projectDir}/build/createSrgToMcp/output.srg")
            mods.create(modId).sources(sourceSets["main"], project(":shared").sourceSets["main"])
        }
    })
}

tasks.withType<JavaCompile> {
    source(project(":shared").sourceSets.main.get().allSource)
}

dependencies {
    minecraft("net.minecraftforge:forge:${minecraftVersion}-${forgeVersion}")
    compileOnly(project(":shared"))

    compileOnly(libs.geyser.api)
    compileOnly(libs.geyser.core) {
        exclude(group = "io.netty")
    }
}

tasks.named<Jar>("jar") {
    finalizedBy("reobfJar")
}