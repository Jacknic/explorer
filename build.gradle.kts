plugins {
    id("org.springframework.boot").version("2.1.4.RELEASE")
    id("org.jetbrains.kotlin.jvm").version("1.3.30")
    id("org.jetbrains.kotlin.plugin.spring").version("1.3.30")
}

apply(plugin= "io.spring.dependency-management")

group = "com.jacknic"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("io.springfox:springfox-swagger2:2.6.1")
    implementation("io.springfox:springfox-swagger-ui:2.6.1")
}