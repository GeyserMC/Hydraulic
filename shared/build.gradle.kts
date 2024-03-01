architectury {
    common("neoforge", "fabric")
}

repositories {
    // TODO: Remove (Temp code)
    flatDir {
        dirs(rootDir.path + "/libs")
    }
}

dependencies {
    compileOnly(libs.mixin)
    // compileOnly(libs.geyser.api)
    // compileOnly(libs.geyser.core) {
    //     exclude(group = "io.netty")
    // }

    // TODO: Remove this once custom block support
    //       is merged into the master branch. This is
    //       far from ideal, but is the easiest way to
    //       get this working for the time being.
    compileOnly(":Geyser-Fabric")

    implementation(libs.auto.service)
    annotationProcessor(libs.auto.service)
}

// TODO: WTF is calling this task?
tasks.create("prepareWorkspace") {
}