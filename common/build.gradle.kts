plugins {
    id("microservices.java-conventions")
}

dependencies {
    compileOnly("org.springframework:spring-webmvc")

    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("com.auth0:java-jwt:4.4.0")
}
