import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.20"
    `maven-publish`
    `java-library`
}

group = "com.github.robbiebowman"
version = "v1.2"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

publishing {
    publications {
        create<MavenPublication>("WordleSolver") {
            from(components["java"])
        }
    }
}