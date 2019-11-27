import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

//import kotlin.math.sign

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
    //signing
}

group = "spring-boot-kotlin-awesome-lib"
version = "0.0.5"
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

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
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
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        publications {
            create<MavenPublication>("mavenJava") {
                //groupId = "com.github.tsarenkotxt"
                groupId = "demo"
                //artifactId = "spring-boot-kotlin-awesome-lib"
                artifactId = "demo"
                version = "0.0.5"

                from(components["java"])
                artifact(dokkaJar)
                artifact(sourcesJar)

                pom {
                    name.set("My awesome lib demo")
                    description.set("Demo with Github Actions/Packages, Spring Boot, Kotlin")
                    url.set("https://github.com/tsarenkotxt/spring-boot-github-actions-demo")
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("tsarenkotxt")
                            name.set("Tsarenko Andrey")
                            email.set("tsarenkotxt@gmail.com")
                        }
                    }
                    scm {
                        url.set("https://github.com/tsarenkotxt/spring-boot-github-actions-demo")
                        connection.set("scm:git:git://github.com/tsarenkotxt/spring-boot-github-actions-demo.git")
                        developerConnection.set("scm:git:git@github.com:tsarenkotxt/spring-boot-github-actions-demo.git")
                    }
                    issueManagement {
                        url.set("https://github.com/tsarenkotxt/spring-boot-github-actions-demo/issues")
                        system.set("GitHub Issues")
                    }
                }
            }
        }
    }
}

/*signing {
    useGpgCmd()
    sign(publishing.publications["mavenJava"])
}*/
