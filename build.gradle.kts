/* This is free and unencumbered software released into the public domain */

/* ------------------------------ Plugins ------------------------------ */
plugins {
    id("java") // Import Java plugin.
    id("java-library") // Import Java Library plugin.
    id("com.diffplug.spotless") version "8.1.0" // Import Spotless plugin.
    id("com.gradleup.shadow") version "8.3.9" // Import Shadow plugin.
    id("checkstyle") // Import Checkstyle plugin.
    eclipse // Import Eclipse plugin.
    kotlin("jvm") version "2.1.21" // Import Kotlin JVM plugin.
    id("io.freefair.lombok") version "8.13.1" // Automatic lombok support.
}

/* --------------------------- JDK / Kotlin ---------------------------- */
java {
    sourceCompatibility = JavaVersion.VERSION_17 // Compile with JDK 17 compatibility.
    toolchain { // Select Java toolchain.
        languageVersion.set(JavaLanguageVersion.of(17)) // Use JDK 17.
        vendor.set(JvmVendorSpec.GRAAL_VM) // Use GraalVM CE.
    }
}

kotlin { jvmToolchain(17) }

/* ----------------------------- Metadata ------------------------------ */
group = "net.trueog.staff-og" // Declare bundle identifier.

version = "1.0" // Declare plugin version (will be in .jar).

val apiVersion = "1.19" // Declare minecraft server target version.

/* ----------------------------- Resources ----------------------------- */
tasks.named<ProcessResources>("processResources") {
    val props = mapOf("version" to version, "apiVersion" to apiVersion)
    inputs.properties(props) // Indicates to rerun if version changes.
    filesMatching("plugin.yml") { expand(props) }
    from("LICENSE") { into("/") } // Bundle licenses into jarfiles.
}

/* ---------------------------- Repos ---------------------------------- */
repositories {
    mavenCentral() // Import the Maven Central Maven Repository.
    gradlePluginPortal() // Import the Gradle Plugin Portal Maven Repository.
    maven { url = uri("https://repo.purpurmc.org/snapshots") } // Import the PurpurMC Maven Repository.
    maven { url = uri("https://jitpack.io") } // Import the Jitpack Maven Repository.
}

/* ---------------------- Java project deps ---------------------------- */
dependencies {
    compileOnly("com.google.code.gson:gson:2.10.1") // Import google gson API.
    compileOnly("commons-io:commons-io:2.11.0") // Import google commons API.
    compileOnly("org.apache.commons:commons-pool2:2.11.1") // Import apache commons API.
    compileOnly("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0") // Import jakarta xml bind API.
    compileOnly("mysql:mysql-connector-java:8.0.32") // Import mysql connector API.
    compileOnly("org.purpurmc.purpur:purpur-api:1.19.4-R0.1-SNAPSHOT") // Declare Purpur API version to be packaged.
    compileOnly("com.github.MilkBowl:VaultAPI:1.7") // Import Vault API.
    implementation("org.jooq:jooq:3.18.4") // Import jooq API.
    implementation("org.reactivestreams:reactive-streams:1.0.4") // Import reactive streams API.
    implementation("org.mariadb.jdbc:mariadb-java-client:3.1.3") // Import JDBC API.
    implementation("io.r2dbc:r2dbc-spi:1.0.0.RELEASE") // Import R2DBC Interface.
    implementation("org.mariadb:r2dbc-mariadb:1.3.0") // Import R2DBC driver.
}

/* ---------------------- Utility functions ---------------------------- */

// Update the plugin.yml version.
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

/* ---------------------- Reproducible jars ---------------------------- */
tasks.withType<AbstractArchiveTask>().configureEach { // Ensure reproducible .jars
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

/* ----------------------------- Shadow -------------------------------- */
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

tasks.jar { archiveClassifier.set("part") } // Applies to root jarfile only.

tasks.build { dependsOn(tasks.spotlessApply, tasks.shadowJar) } // Build depends on spotless and shadow.

/* --------------------------- Javac opts ------------------------------- */
tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-parameters") // Enable reflection for java code.
    options.isFork = true // Run javac in its own process.
    options.compilerArgs.add("-Xlint:deprecation") // Trigger deprecation warning messages.
    options.encoding = "UTF-8" // Use UTF-8 file encoding.
}

/* ----------------------------- Auto Formatting ------------------------ */
spotless {
    java {
        eclipse().configFile("config/formatter/eclipse-java-formatter.xml") // Eclipse java formatting.
        leadingTabsToSpaces() // Convert leftover leading tabs to spaces.
        removeUnusedImports() // Remove imports that aren't being called.
    }
    kotlinGradle {
        ktfmt().kotlinlangStyle().configure { it.setMaxWidth(120) } // JetBrains Kotlin formatting.
        target("build.gradle.kts", "settings.gradle.kts") // Gradle files to format.
    }
}

checkstyle {
    toolVersion = "10.18.1" // Declare checkstyle version to use.
    configFile = file("config/checkstyle/checkstyle.xml") // Point checkstyle to config file.
    isIgnoreFailures = true // Don't fail the build if checkstyle does not pass.
    isShowViolations = true // Show the violations in any IDE with the checkstyle plugin.
}

tasks.named("compileJava") {
    dependsOn("spotlessApply") // Run spotless before compiling with the JDK.
}

tasks.named("spotlessCheck") {
    dependsOn("spotlessApply") // Run spotless before checking if spotless ran.
}

/* ------------------------------ Eclipse SHIM ------------------------- */

// This can't be put in eclipse.gradle.kts because Gradle is weird.
subprojects {
    apply(plugin = "java-library")
    apply(plugin = "eclipse")
    eclipse.project.name = "${project.name}-${rootProject.name}"
    tasks.withType<Jar>().configureEach { archiveBaseName.set("${project.name}-${rootProject.name}") }
}
