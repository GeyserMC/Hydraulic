architectury {
    common("neoforge", "fabric")
}

dependencies {
    compileOnly(libs.mixin)
    compileOnly(libs.mixinextras)
    compileOnly(libs.geyser.api)
    compileOnly(libs.geyser.core) {
        exclude(group = "io.netty")
        exclude(group = "io.netty.incubator")
    }
    // Examinable and friends - the published Geyser poms do not always surface transitive
    // dependencies like Kyori's examination on the loom transform path.
    compileOnly("net.kyori:examination-api:1.3.0")

    api(libs.pack.converter)

    implementation(libs.auto.service)
    annotationProcessor(libs.auto.service)

    // Only here to suppress "unknown enum constant EnvType.CLIENT" warnings.
    compileOnly(libs.fabric.loader)
}
