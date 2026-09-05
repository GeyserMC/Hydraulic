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

    api(libs.pack.converter)
    compileOnly(libs.examination.api)

    implementation(libs.auto.service)
    annotationProcessor(libs.auto.service)

    annotationProcessor(libs.configurate.`interface`.ap)
    compileOnly(libs.bundles.configurate)

    // Only here to suppress "unknown enum constant EnvType.CLIENT" warnings.
    compileOnly(libs.fabric.loader)
}
