architectury {
    common("neoforge", "fabric")
}

dependencies {
    compileOnly(libs.mixin)
    compileOnly(libs.geyser.api)
    compileOnly(libs.geyser.core) {
        exclude(group = "io.netty")
    }

    implementation(libs.auto.service)
    annotationProcessor(libs.auto.service)

    implementation(libs.pack.converter)
}

// TODO: WTF is calling this task?
tasks.create("prepareWorkspace") {
}