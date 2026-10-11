plugins {
    id("dev.opencollab.conduit")
}

architectury {
    common("neoforge", "fabric")
}

conduit {
    targetPackage = "org.geysermc.hydraulic.entity.conduit"

    contextJars(loom.namedMinecraftJars)
    context(libs.joml, libs.architectury.api)

    classes(
        "net.minecraft.resources.Identifier",
        "net.minecraft.resources.ResourceKey",
        "net.minecraft.client.model.geom.ModelLayerLocation",
        "net.minecraft.client.model.geom.builders.LayerDefinition",
        "net.minecraft.client.model.geom.builders.MaterialDefinition",
        "net.minecraft.client.model.geom.builders.MeshDefinition",
        "net.minecraft.client.model.geom.builders.PartDefinition",
        "net.minecraft.client.model.geom.builders.CubeListBuilder",
        "net.minecraft.client.model.geom.builders.CubeDefinition",
        "net.minecraft.client.model.geom.PartPose",
        "net.minecraft.client.model.geom.builders.CubeDeformation",
        "net.minecraft.client.model.geom.builders.UVPair",
        "net.minecraft.client.renderer.entity.EntityRendererProvider",
        "net.minecraft.client.renderer.entity.LivingEntityRenderer",
        "net.minecraft.world.entity.EntityType",
        "net.minecraft.world.entity.EntityType\$Builder",
        "net.minecraft.world.entity.EntityType\$EntityFactory",
        "org.joml.Vector3f",
        "org.joml.Vector3fc"
    )
}

dependencies {
    compileOnly(libs.mixin)
    compileOnly(libs.mixinextras)
    compileOnly(libs.geyser.api)
    compileOnly(libs.geyser.core) {
        exclude(group = "io.netty")
        exclude(group = "io.netty.incubator")
    }
    compileOnly(libs.adventure.api)
    api(libs.adventure.examination)

    api(libs.pack.converter)
    compileOnly(libs.examination.api)

    implementation(libs.auto.service)
    annotationProcessor(libs.auto.service)

    compileOnly(libs.bundles.configurate)

    // Only here to suppress "unknown enum constant EnvType.CLIENT" warnings.
    compileOnly(libs.fabric.loader)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.mojava)
    testRuntimeOnly(libs.junit.launcher)
}

tasks.test {
    useJUnitPlatform()
    // The test mod is scanned the way a mod's jar would be, from its compiled classes.
    val testMod = project(":test").layout.buildDirectory.dir("classes/java/main")
    dependsOn(":test:compileJava")
    inputs.dir(testMod)
    jvmArgumentProviders.add(CommandLineArgumentProvider { listOf("-Dhydraulic.test.mod=" + testMod.get().asFile.absolutePath) })
}

// Vanilla's entity models are only in the client jar, so they are read at build time and shipped for mods that reuse them.
val generator: SourceSet by sourceSets.creating {
    compileClasspath += sourceSets.main.get().output.classesDirs + sourceSets.main.get().compileClasspath
    runtimeClasspath += output + compileClasspath
}

val generateVanillaModels by tasks.registering(JavaExec::class) {
    description = "Reads vanilla's entity models from the client jar into the bundle Hydraulic ships."
    val bundle = layout.buildDirectory.dir("generated/vanilla-models")
    classpath = generator.runtimeClasspath
    mainClass = "org.geysermc.hydraulic.generator.VanillaModelsGenerator"
    javaLauncher = javaToolchains.launcherFor(java.toolchain)
    inputs.files(loom.namedMinecraftJars)
    outputs.dir(bundle)
    argumentProviders.add(CommandLineArgumentProvider {
        listOf(bundle.get().file("hydraulic/vanilla-models.json").asFile.absolutePath) + loom.namedMinecraftJars.files.map { it.absolutePath }
    })
}

sourceSets.main {
    resources.srcDir(generateVanillaModels)
}
