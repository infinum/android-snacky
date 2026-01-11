plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

apply {
    from("$rootDir/config.gradle.kts")
    from("$rootDir/dokka.gradle")
    from("$rootDir/maven-publish.gradle")
    from("$rootDir/detekt.gradle")
}

val buildConfig: Map<String, Any> by project
val releaseConfig: Map<String, Any> by project
val sonatype: Map<String, Any> by project

android {
    namespace = "com.infinum.android.snacky"
    compileSdk = buildConfig["compileSdk"] as Int

    defaultConfig {
        minSdk = buildConfig["minSdk"] as Int

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    kotlin {
        explicitApi()
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

extra["mavenPublishProperties"] = mapOf(
    "group" to releaseConfig["group"],
    "version" to releaseConfig["version"],
    "artifactId" to "snacky",
    "repository" to mapOf(
        "url" to sonatype["url"],
        "username" to sonatype["username"],
        "password" to sonatype["password"]
    ),
    "name" to "Snacky Android",
    "description" to "Module for creating customizable snackbars in Compose apps",
    "url" to "https://github.com/infinum/android-snacky",
    "scm" to mapOf(
        "connection" to "https://github.com/infinum/android-snacky.git",
        "url" to "https://github.com/infinum/android-snacky"
    )
)

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.material3.android)

    // Testing dependencies
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    androidTestImplementation(libs.androidx.junit.ext)
}

// Configure test tasks to show detailed output in console
tasks.withType<Test> {
    testLogging {
        // Show test events in console
        events("passed", "skipped", "failed", "standardOut", "standardError")
        
        // Show detailed information for failed tests
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showExceptions = true
        showCauses = true
        showStackTraces = true
        
        // Show standard output and error streams
        showStandardStreams = false
        
        // Display summary after test execution
        afterSuite(KotlinClosure2<TestDescriptor, TestResult, Unit>({ desc, result ->
            if (desc.parent == null) { // Only execute for the overall test suite
                println("\nTest Results: ${result.resultType}")
                println("Tests run: ${result.testCount}, " +
                        "Passed: ${result.successfulTestCount}, " +
                        "Failed: ${result.failedTestCount}, " +
                        "Skipped: ${result.skippedTestCount}")
            }
        }))
    }
}