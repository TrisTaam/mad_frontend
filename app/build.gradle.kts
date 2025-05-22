import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kotlin.plugin.serialization)
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.mobile6"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mobile6"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    val localProperties = Properties().also {
        it.load(FileInputStream(rootProject.file("local.properties")))
    }

    defaultConfig {
        val baseHost = localProperties.getProperty("BASE_HOST")
        val basePort = localProperties.getProperty("BASE_PORT")
        resValue("string", "base_host", baseHost)
//        buildConfigField(
//            "String",
//            "BASE_URL",
//            "\"$baseHost:$basePort\""
//        )
        buildConfigField(
            "String",
            "BASE_URL",
            "\"http://35.198.232.229:8080\""
        )
    }
}

dependencies {
    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // Glide
    implementation(libs.glide)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)

    // OkHttp
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Timber
    implementation(libs.timber)

    // Kotlinx Serialization
    implementation(libs.kotlinx.serialization.json)

    // DataStore
    implementation(libs.datastore.preferences)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // QR Code generation and scanning
    implementation(libs.zxing.core)
    implementation(libs.play.services.vision)

    // Camera
    implementation(libs.camera.camera2)
    implementation(libs.camera.lifecycle)
    implementation(libs.camera.view)

    // MP Android Chart
    implementation(libs.mpandroidchart)

    // Kizitonwose Calendar
    implementation(libs.kizitonwose.calendar.view)
}