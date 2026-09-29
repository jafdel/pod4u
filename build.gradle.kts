import org.gradle.kotlin.dsl.implementation

plugins {
    java
    application
    "java-gradle-plugin"
    "java-library"
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("org.javamodularity.moduleplugin") version "1.8.15"
    id("org.beryx.jlink") version "2.25.0"
}

group = "org.pod4u"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
    maven { url = uri("https://repo1.maven.org/maven2/") }
}

val junitVersion = "5.12.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainModule.set("org.pod4u.app")
    mainClass.set("org.pod4u.app.Main")
}

javafx {
    version = "21.0.2"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
    implementation("at.favre.lib:bcrypt:0.10.2")
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("com.kohlschutter.junixsocket:junixsocket-core:2.10.1")
    implementation("com.kohlschutter.junixsocket:junixsocket-common:2.10.1")
    implementation("com.kohlschutter.junixsocket:junixsocket-native-common:2.10.1")
    implementation("com.microsoft.sqlserver:mssql-jdbc:13.4.0.jre11")
    implementation(platform("tools.jackson:jackson-bom:3.1.3"))
    implementation("tools.jackson.core:jackson-databind")
    implementation("tools.jackson.core:jackson-core")
    implementation("se.michaelthelin.spotify:spotify-web-api-java:8.4.1")
    implementation("org.slf4j:slf4j-api:2.0.17")
    implementation("ch.qos.logback:logback-classic:1.5.13")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jlink {
    imageZip.set(layout.buildDirectory.file("/distributions/app-${javafx.platform.classifier}.zip"))
    options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
    launcher {
        name = "app"
    }
}
