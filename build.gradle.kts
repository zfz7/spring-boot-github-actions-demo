import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "2.2.1.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"

    id("org.sonarqube") version "2.7.1"
    id("org.jetbrains.dokka") version "0.9.17"

    kotlin("jvm") version "1.3.50"
    kotlin("plugin.spring") version "1.3.50"

    idea
    jacoco
    `maven-publish`
}

group = "spring-boot-kotlin-awesome-lib"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<JacocoReport> {
    reports {
        xml.isEnabled = true
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks {
    dokka {
        outputFormat = "html"
        outputDirectory = "$buildDir/javadoc"
        moduleName = rootProject.name
    }
}

val dokkaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    archiveClassifier.set("javadoc")
    from(tasks.dokka)
    dependsOn(tasks.dokka)
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
}

publishing {
    repositories {
        maven {
            name = "Github"
            url = uri("https://maven.pkg.github.com/tsarenkotxt/spring-boot-github-actions-demo")
            credentials {
                username = "tsarenkotxt"
                password = System.getenv("GITHUB_PACKAGES_WRITE_TOKEN")
            }
        }
    }
    publications {
        register("jar", MavenPublication::class) {
            from(components["java"])
            artifact(dokkaJar)
            artifact(sourcesJar)
            pom.withXml {
                asNode().apply {
                    appendNode("name", "spring-boot-kotlin-awesome-lib")
                    appendNode("description", "Demo with Github Actions/Packages, Spring Boot, Kotlin")
                    appendNode("url", "https://github.com/tsarenkotxt/spring-boot-github-actions-demo")
                    appendNode("licenses").appendNode("license").apply {
                        appendNode("name", "The Apache Software License, Version 2.0")
                        appendNode("url", "http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
            }
        }
    }
}
