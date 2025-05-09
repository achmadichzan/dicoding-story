import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinx.serialization)
    id("kotlin-parcelize")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.achmadichzan.dicodingstory"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.achmadichzan.dicodingstory"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            val properties = Properties()
            project.rootProject.file("local.properties").inputStream()
                .use { properties.load(it) }
            buildConfigField(
                "String",
                "BASE_URL",
                "${properties.getProperty("BASE_URL")}"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    secrets {
        propertiesFileName = "secrets.properties"
        defaultPropertiesFileName = "local.defaults.properties"
    }
}

dependencies {

    implementation(libs.bundles.lifecycle) // Lifecycle

    implementation(libs.bundles.koin) // Koin

    implementation(libs.bundles.ktor) // Ktor

    implementation(libs.bundles.navigation) // Navigation

    implementation(libs.bundles.coil) // Coil

    // Room
    implementation(libs.bundles.room)
    ksp(libs.room.compiler)

    implementation(libs.bundles.paging3) // Paging3

    implementation(libs.androidx.datastore.preferences) // Data Store

    implementation(libs.androidx.core.splashscreen) // Splash Screen

    implementation(libs.androidx.material.icons.extended)

    implementation(libs.bundles.camera) // CameraX

    implementation(libs.bundles.maps) // Maps

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}