plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.0.21"
    kotlin("kapt")
    id("androidx.room")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "dev.eknath.jottersspace"
    compileSdk = 35

    defaultConfig {
        applicationId = "dev.eknath.jottersspace"
        minSdk = 24
        targetSdk = 34
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
//        freeCompilerArgs = listOf("-Xemit-jvm-type-annotations") // Required for Annotation Processing or @CatalystName() will not work
        freeCompilerArgs += "-Xjvm-default=all"
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }

    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.messaging.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.android.sdk) //Zoho Catalyst SDK
    implementation(libs.androidx.navigation.compose) //Navigation for Compose
    implementation(libs.kotlinx.serialization.json) //Kotlin Serialization
    implementation(libs.kotlinx.coroutines.android)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson) // Can use kotlin Serialization as well
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    //hilt dependency injection
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    kapt(libs.hilt.android.compiler)

    // Room Dependencies
    val room_version = "2.6.1"

    // Room Database Core
    implementation(libs.androidx.room.runtime)
    // Annotation Processor (KSP)
    ksp(libs.androidx.room.compiler)
    // Kotlin Extensions and Coroutines support for Room (Optional)
    implementation(libs.androidx.room.ktx)

    // Google Services
    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))
}