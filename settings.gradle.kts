// LawyerArchives/settings.gradle.kts
pluginManagement {
    repositories {
        google() // Google's Maven repository
        mavenCentral() // Maven Central repository
        gradlePluginPortal() // Gradle Plugin Portal
        maven { url = uri("https://jitpack.io") } // این خط بسیار مهم است
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google() // Google's Maven repository
        mavenCentral() // Maven Central repository
        maven { url = uri("https://jitpack.io") } // این خط بسیار مهم است
    }
}
// Include the 'app' module in the project
include(":app")
