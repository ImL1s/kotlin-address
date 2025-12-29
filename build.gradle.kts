plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    `maven-publish`
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
        publishLibraryVariants("release")
    }

    jvm()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "address"
        }
    }

    listOf(
        watchosArm64(),
        watchosX64(),
        watchosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "address"
        }
    }

    sourceSets {
        commonMain.dependencies {
            // Depend on kotlin-crypto-pure for key derivation
            // implementation(project(":kotlin-crypto-pure"))
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        androidMain.dependencies {
        }
        iosMain.dependencies {
        }
        watchosMain.dependencies {
        }
        jvmMain.dependencies {
        }
    }
}

android {
    namespace = "io.github.iml1s.address"
    compileSdk = 35
    defaultConfig {
        minSdk = 26
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
