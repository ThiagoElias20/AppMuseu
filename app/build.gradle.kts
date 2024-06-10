plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.agoravai"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.agoravai"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation ("androidx.core:core-ktx:1.7.0")
    implementation ("androidx.viewpager2:viewpager2:1.0.0")

    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")

    implementation ("com.google.firebase:firebase-analytics:21.2.0")

    //implementation ("com.google.firebase:firebase-storage:20.3.0")

    //implementation ("com.google.firebase:firebase-auth:21.3.0")
    implementation ("com.google.firebase:firebase-appcheck:17.0.0") // Adicione esta linha
    implementation ("com.firebaseui:firebase-ui-storage:8.0.0") // Para o Firebase UI Storage, se estiver usando

    //implementation ("com.google.firebase:firebase-storage:20.0.1")
    //implementation ("com.github.dhaval2404:imagepicker:2.1")
    implementation ("androidx.activity:activity-ktx:1.3.1")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}