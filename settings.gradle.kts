pluginManagement {
    repositories {
        google() // Repository for Android Gradle Plugin
        gradlePluginPortal()
        mavenCentral() // Repository for Maven artifacts
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS) // Enforce the use of settings repositories
    repositories {
        google() // Use Google's repository for project dependencies
        mavenCentral() // Use Maven Central for project dependencies
    }
}

rootProject.name = "Log Voice"
include(":app") // Include your modules
