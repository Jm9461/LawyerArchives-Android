    pluginManagement {
        repositories {
            google()
            mavenCentral()
            gradlePluginPortal()
        }
    }

    dependencyResolutionManagement {
        repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
        repositories {
            google()
            mavenCentral()
            // اطمینان حاصل کنید که این خط وجود دارد و درست است
            maven { url = uri("https://jitpack.io") }
        }
    }

    rootProject.name = "LawyerArchives"
    include(":app")
    