import com.google.protobuf.gradle.id

plugins {
    kotlin("jvm") version "2.3.20"
    id("com.gradleup.shadow") version "8.3.9"
    id("com.google.protobuf") version "0.10.0"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
    compileOnly("org.apache.logging.log4j:log4j-core:2.26.0")
    implementation("com.google.protobuf:protobuf-kotlin:4.34.1")
    implementation("io.grpc:grpc-stub:1.81.0")
    implementation("io.grpc:grpc-kotlin-stub:1.5.0")
    implementation("io.grpc:grpc-netty-shaded:1.81.0")
    implementation("io.grpc:grpc-protobuf:1.81.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
}

sourceSets {
    main {
        java {
            srcDirs("build/generated/source/proto/main/grpc")
            srcDirs("build/generated/source/proto/main/grpckt")
            srcDirs("build/generated/source/proto/main/java")
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:4.34.1"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.81.0"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:1.5.0:jdk8@jar"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                // Apply the "grpc" plugin whose spec is defined above, without options.
                id("grpc")
                id("grpckt")
            }
            it.builtins {
                id("kotlin")
            }
        }
    }
}

kotlin {
    jvmToolchain(17)
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    processResources {
        val props = mapOf("version" to version)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    shadowJar {
        archiveClassifier.set("") // Use empty string instead of null.
        isEnableRelocation = true
        relocationPrefix = "${project.group}.shadow"
        exclude("io.github.miniplaceholders.*") // Exclude the MiniPlaceholders package from being shadowed.
        exclude("natives/**")
        mergeServiceFiles()
        minimize {
            exclude(dependency("org.slf4j:slf4j-nop:.*"))
            exclude(dependency("io.grpc:grpc-netty-shaded:.*"))
        }
    }

    jar { archiveClassifier.set("part") }

    build { dependsOn(shadowJar) }
}
