plugins {
	java
	id("org.springframework.boot") version "3.2.4"
	id("io.spring.dependency-management") version "1.1.4"
	id("io.freefair.lombok") version "8.6"
	id("checkstyle")
}

group = "io.github.kevinlaig"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("com.auth0:java-jwt:4.4.0")
	implementation("org.mapstruct:mapstruct:1.5.5.Final")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.1")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
	runtimeOnly("com.mysql:mysql-connector-j")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("com.h2database:h2")
}


checkstyle {
	toolVersion = "10.14.2"
	config = resources.text.fromFile("google_checks.xml")
}
tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<Checkstyle> {
	reports {
		html.required.set(true)
		xml.required.set(true)
	}
}