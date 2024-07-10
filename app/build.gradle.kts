plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.secrets.gradle.plugin)

}

android {
    namespace = "br.com.tmg.ecotrack"
    compileSdk = 34

    defaultConfig {
        applicationId = "br.com.tmg.ecotrack"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
    implementation(libs.androidx.navigation.compose)
    implementation(libs.firebase.common.ktx)
    implementation(libs.firebase.auth.ktx)


    //TESTES UNITARIOS
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    //DEPENDENCIAS COMUNICACAO WEB
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation( libs.adapter.rxjava2)


    //BANCO E DADOS ROOM
    implementation (libs.androidx.room.runtime)
    implementation(libs.androidx.monitor)



    // GOOGLE MAPS
    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)
    implementation(libs.secrets.gradle.plugin)
    implementation(libs.maps.compose)



    //FIREBASE
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)


    // Koin for Android
    implementation (libs.koin.android)
    implementation (libs.koin.androidx.compose)

    //SPLASH SCREEN
    implementation(libs.androidx.core.splashscreen)



}