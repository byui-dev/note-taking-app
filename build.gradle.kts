plugins {
    // Kotlin JVM plugin
    kotlin("jvm") version "1.9.21"
    
    // Kotlin serialization plugin
    kotlin("plugin.serialization") version "1.9.21"

    // Application plugin to run the app
    application
}

repositories {
    // Use Maven Central to fetch dependencies
    mavenCentral()
}

dependencies {
    // Kotlin standard library
    implementation(kotlin("stdlib"))

    // Kotlin test library for unit tests
    testImplementation(kotlin("test"))

    // Kotlinx Serialization JSON library for export/import
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
}

application {
    // Define the main class to run
    mainClass.set("com.project.notetaking.MainKt")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.project.notetaking.MainKt"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
