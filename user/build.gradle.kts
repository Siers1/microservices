plugins {
    kotlin("kapt")
    id("microservices.boot-conventions")
}

dependencies {
    implementation(project(":common"))

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("tools.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.security:spring-security-crypto")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("com.mybatis-flex:mybatis-flex-spring-boot4-starter:1.11.8")
    kapt("com.mybatis-flex:mybatis-flex-processor:1.11.8")
    runtimeOnly("com.mysql:mysql-connector-j")
}
