architectury {
    common("neoforge", "fabric")
}

dependencies {
    compileOnly(libs.mixin)
    compileOnly(libs.mixinextras)
    compileOnly(libs.asm)
    compileOnly(libs.geyser.api)
    compileOnly(libs.geyser.core) {
        exclude(group = "io.netty")
    }

    api(libs.pack.converter)

    implementation(libs.auto.service)
    annotationProcessor(libs.auto.service)

    // Only here to suppress "unknown enum constant EnvType.CLIENT" warnings.
    compileOnly(libs.fabric.loader)
}
