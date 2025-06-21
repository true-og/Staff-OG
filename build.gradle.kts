plugins {
    id("java") // Tell gradle this is a java project.
    id("java-library") // Import helper for source-based libraries.
    id("com.diffplug.spotless") version "7.0.4" // Import auto-formatter.
    id("com.gradleup.shadow") version "8.3.6" // Import shadow API.
    eclipse // Import eclipse plugin for IDE integration.
    id("io.freefair.lombok") version "8.13.1" // Automatic lombok support.
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
        vendor.set(JvmVendorSpec.GRAAL_VM)
    }
}

group = "uk.hotten.staffog" // Declare bundle identifier.

version = "1.0-beta" // Declare plugin version (will be in .jar).

val apiVersion = "1.19" // Declare minecraft server target version.

tasks.named<ProcessResources>("processResources") {
    val props = mapOf("version" to version, "apiVersion" to apiVersion)

    inputs.properties(props) // Indicates to rerun if version changes.

    filesMatching("plugin.yml") { expand(props) }
    from("LICENSE") { // Bundle license into .jars.
        into("/")
    }
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven { url = uri("https://repo.purpurmc.org/snapshots") }
    maven { url = uri("https://repo.purpurmc.org/snapshots") }
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    compileOnly("com.google.code.gson:gson:2.10.1") // Import google gson API.
    compileOnly("commons-io:commons-io:2.11.0") // Import google commons API.
    compileOnly("org.apache.commons:commons-pool2:2.11.1") // Import apache commons API.
    compileOnly("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0") // Import jakarta xml bind API.
    compileOnly("mysql:mysql-connector-java:8.0.32") // Import mysql connector API.
    compileOnly("org.purpurmc.purpur:purpur-api:1.19.4-R0.1-SNAPSHOT") // Declare purpur API version to be packaged.
    compileOnly("com.github.MilkBowl:VaultAPI:1.7") // Import Vault API.
    implementation("org.jooq:jooq:3.18.4") // Import jooq API.
    implementation("org.reactivestreams:reactive-streams:1.0.4") // Import reactive streams API.
    implementation("org.mariadb.jdbc:mariadb-java-client:3.1.3") // Import JDBC API.
    implementation("io.r2dbc:r2dbc-spi:1.0.0.RELEASE") // Import R2DBC Interface.
    implementation("org.mariadb:r2dbc-mariadb:1.3.0") // Import R2DBC driver.
}

// Utility functions
fun revertPluginYmlVersion() {
    val yaml = file("src/main/resources/plugin.yml")
    val lines = yaml.readLines().toMutableList()
    lines[2] = "version: $version"
    yaml.writeText(lines.joinToString("\n"))
}

// Add the name of the current git branch to the build.
fun currentGitBranch(): String =
    try {
        val proc =
            ProcessBuilder("git", "-C", rootProject.projectDir.absolutePath, "rev-parse", "--abbrev-ref", "HEAD")
                .redirectErrorStream(true)
                .start()

        val out = proc.inputStream.bufferedReader().readText().trim()
        proc.waitFor()

        // If Git failed or gave us garbage, fall back.
        val branch = if (proc.exitValue() != 0 || out.startsWith("fatal")) "ogsuite" else out

        // Keep the classifier file-system & URI safe
        branch.lowercase().replace(Regex("[^a-z0-9._-]"), "_").ifBlank { "ogsuite" }
    } catch (_: Exception) {
        "ogsuite"
    }

// Update plugin.yml before compilation
val updatePluginYmlVersion =
    tasks.register("updatePluginYmlVersion") {
        doLast {
            val yaml = file("src/main/resources/plugin.yml")
            val lines = yaml.readLines().toMutableList()
            lines[2] = "version: $version-${currentGitBranch()}"
            yaml.writeText(lines.joinToString("\n"))
        }
    }

tasks.withType<AbstractArchiveTask>().configureEach { // Ensure reproducible .jars
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

tasks.shadowJar {
    archiveClassifier.set(currentGitBranch())
    from("LICENSE") { into("/") }
    dependencies {
        include(dependency("org.jooq:jooq"))
        include(dependency("org.reactivestreams:reactive-streams"))
        include(dependency("org.mariadb.jdbc:mariadb-java-client"))
        include(dependency("io.r2dbc:r2dbc-spi"))
        include(dependency("org.mariadb:r2dbc-mariadb"))
    }
    minimize {
        exclude(dependency("org.mariadb.jdbc:mariadb-java-client"))
        exclude(dependency("io.r2dbc:r2dbc-spi"))
        exclude(dependency("org.mariadb:r2dbc-mariadb"))
    }
    doLast { revertPluginYmlVersion() }
}

tasks.named("compileJava") { dependsOn(updatePluginYmlVersion) }

tasks.build {
    dependsOn(tasks.spotlessApply)
    dependsOn(tasks.shadowJar)
}

tasks.jar { archiveClassifier.set("part") }

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-parameters")
    options.compilerArgs.add("-Xlint:deprecation") // Triggers deprecation warning messages.
    options.encoding = "UTF-8"
    options.isFork = true
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
        vendor = JvmVendorSpec.GRAAL_VM
    }
}

spotless {
    java {
        removeUnusedImports()
        palantirJavaFormat()
    }
    kotlinGradle {
        ktfmt().kotlinlangStyle().configure { it.setMaxWidth(120) }
        target("build.gradle.kts", "settings.gradle.kts")
    }
}
