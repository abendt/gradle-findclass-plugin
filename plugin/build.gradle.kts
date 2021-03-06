plugins {
    `java-gradle-plugin`
    id("maven-publish")
    id("com.gradle.plugin-publish") version "0.16.0"

    id("org.jetbrains.kotlin.jvm") version "1.5.31"
}

group = "io.github.redgreencoding"
version = "0.0.1"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.barfuin.texttree:text-tree:2.1.1")

    testImplementation("org.jetbrains.kotlin:kotlin-test")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

gradlePlugin {
    val findClass by plugins.creating {
        id = "io.github.redgreencoding.findclass"
        implementationClass = "io.github.redgreencoding.findclass.FindClassGradlePlugin"
    }
}

pluginBundle {
    website = "https://github.com/red-green-coding/findclass-gradle-plugin"
    vcsUrl = "https://github.com/red-green-coding/findclass-gradle-plugin.git"
    tags = listOf("findclass", "searchclass", "dependencies", "analyze")
}

publishing {
    repositories {
        maven {
            name = "localPluginRepository"
            url = uri("../build/local-plugin-repository")
        }
    }
}

val functionalTestSourceSet = sourceSets.create("functionalTest") {
}

gradlePlugin.testSourceSets(functionalTestSourceSet)
configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])

val functionalTest by tasks.registering(Test::class) {
    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath
}

tasks.check {
    dependsOn(functionalTest)
}
