plugins {
    id("java")
    application
}

group = "de.uulm.in.vs.grn.p2a"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}
application {
    mainClass = "de.uulm.in.vs.grn.p4a.ReversedServiceServer"
}

tasks.test {
    useJUnitPlatform()
}