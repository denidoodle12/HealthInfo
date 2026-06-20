plugins {
    alias(libs.plugins.androidDynamicFeature)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinParcelize)
}

android {
    namespace = "com.expert.healthinfo.favorite"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles("proguard-rules.pro")
        }
        debug {
            proguardFiles("proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

// Force resolve annotation dependency conflict:
// :core uses annotation:1.9.1 but :favorite's debugRuntimeClasspath resolves to 1.8.1
// causing a strict constraint conflict during lint analysis.
configurations.all {
    resolutionStrategy {
        force("androidx.annotation:annotation:1.8.1")
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":app"))
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
