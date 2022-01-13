import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id("maven-publish")
    id("signing")
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

val nexusUsername: String? by project
val nexusPassword: String? by project

group = "com.nbottarini"
version = "0.5"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("org.assertj:assertj-core:3.21.0")
    testImplementation("net.bytebuddy:byte-buddy:1.11.15") // Added for mockk compatibility with JDK16
    testImplementation("io.mockk:mockk:1.12.0")
}

tasks.withType<KotlinCompile> { kotlinOptions.jvmTarget = "1.8" }

kotlin {
    sourceSets["main"].apply {
        kotlin.srcDirs("src")
        resources.srcDirs("resources")
    }
    sourceSets["test"].apply {
        kotlin.srcDir("test")
        resources.srcDir("test_resources")
    }
}

java {
    withJavadocJar()
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8

    sourceSets["main"].apply {
        java.srcDirs("src")
        resources.srcDirs("resources")
    }
    sourceSets["test"].apply {
        java.srcDir("test")
        resources.srcDir("test_resources")
    }
}

tasks.withType<Test> { useJUnitPlatform() }

tasks.withType<Test>().configureEach {
    reports.html.required.set(false)
    reports.junitXml.required.set(false)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "asimov-json"
            from(components["java"])

            pom {
                name.set("Json")
                description.set("Small and fast kotlin library for reading and writing json")
                url.set("https://github.com/nbottarini/asimov-json-kt")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("http://www.opensource.org/licenses/mit-license.php")
                    }
                }

                developers {
                    developer {
                        id.set("nbottarini")
                        name.set("Nicolas Bottarini")
                        email.set("nicolasbottarini@gmail.com")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/asimov-json-kt.git")
                    developerConnection.set("scm:git:ssh://github.com/asimov-json-kt.git")
                    url.set("https://github.com/nbottarini/asimov-json-kt")
                }
            }
        }
    }
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            username.set(nexusUsername)
            password.set(nexusPassword)
        }
    }
}

signing {
    sign(publishing.publications["maven"])
}
