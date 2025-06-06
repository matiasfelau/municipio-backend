val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val hikaricp_version: String by project
val ehcache_version: String by project
val exposed_version: String by project

plugins {
    kotlin("jvm") version "1.9.23"
    id("io.ktor.plugin") version "2.3.9"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.23"

}

group = "ar.edu.uade"
version = "0.0.1"

application {
    mainClass.set("ar.edu.uade.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    //database
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("mysql:mysql-connector-java:8.0.28")
    implementation("com.zaxxer:HikariCP:$hikaricp_version")
    implementation("org.ehcache:ehcache:$ehcache_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")

    implementation("com.microsoft.sqlserver:mssql-jdbc:6.1.0.jre7")

    //security
    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")

    //mails
    implementation("io.ktor:ktor-server-netty:2.0.0")
    implementation("io.ktor:ktor-server-html-builder:2.0.0")
    implementation("io.ktor:ktor-server-mustache:2.0.0")
    implementation("io.ktor:ktor-server-call-logging:2.0.0")
    implementation("com.sun.mail:javax.mail:1.6.2")

    //cloudinary
    implementation("com.cloudinary:cloudinary-http44:1.38.0")
    implementation("com.cloudinary:cloudinary-taglib:1.38.0")
    implementation("io.github.cdimascio:dotenv-java:2.2.4")

    //notis
    implementation("com.google.firebase:firebase-admin:9.0.0") {
        exclude(group = "com.google.guava", module = "listenablefuture")
    } // check for the latest version


    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    
}
