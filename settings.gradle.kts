pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {
            version("kotlin", "2.1.0")
            version("androidGradlePlugin", "8.5.2")
            
            plugin("kotlin-multiplatform", "org.jetbrains.kotlin.multiplatform").versionRef("kotlin")
            plugin("android-library", "com.android.library").versionRef("androidGradlePlugin")
        }
    }
}

rootProject.name = "kotlin-address"

includeBuild("../kotlin-crypto-pure")
