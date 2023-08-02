import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    id("com.github.weave-mc.weave-gradle") version "649dba7468"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "pl.kacorvixon.blue"
version = "1.0"

minecraft.version("1.8.9")

repositories {
    maven("https://jitpack.io")
    maven("https://repo.spongepowered.org/maven/")
    maven ("https://raw.githubusercontent.com/kotlin-graphics/mary/master")
    maven ("https://dl.bintray.com/kotlin/kotlin-dev")
}
dependencies {
    compileOnly("com.github.weave-mc:weave-loader:70bd82faa6")
    compileOnly("org.spongepowered:mixin:0.8.5")
    compileOnly("javax.vecmath:vecmath:1.5.2")
}
tasks.compileJava {
    options.release.set(11)
}