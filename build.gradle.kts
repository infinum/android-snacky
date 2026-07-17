plugins {
    alias(libs.plugins.detekt.plugin) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose.compiler) apply false
}

buildscript {
    apply(from = "maven.gradle")

    dependencies {
        classpath(libs.tools.gradle)
        classpath(libs.kotlin.plugin)
        classpath(libs.dokka.plugin)
    }
}
