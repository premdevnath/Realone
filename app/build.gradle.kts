import org.gradle.kotlin.dsl.android

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.kotlin.parcelize) // Add this line
    alias(libs.plugins.dagger.hilt) // Apply false only if needed globa
    kotlin("kapt")
  //  id("com.google.dagger.hilt.android") // Hilt plugin
    id("kotlin-kapt") // Room ke liye ye plugin chahiye
    // Compiler dependency add karo
}

android {
    namespace = "com.example.psee"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.psee"
        minSdk = 28
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
    }
    buildFeatures {
        compose = true

    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/DEPENDENCIES"
            excludes += "/META-INF/LICENSE"
            excludes += "/META-INF/LICENSE.txt"
            excludes += "/META-INF/NOTICE"
            excludes += "/META-INF/NOTICE.txt"
            excludes += "mozilla/public-suffix.list.txt"
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
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    /*
    //deagger
    implementation("com.google.dagger:hilt-android:2.44") // Hilt dependency
    kapt("com.google.dagger:hilt-compiler:2.44") // Hilt compiler for annotation processing
    // Hilt for Jetpack Compose
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("androidx.core:core-ktx:1.10.1") // Core KTX
    //

     */

    kapt(libs.dagger.hilt.compiler)
    implementation(libs.dagger.hilt.compose)
    implementation(libs.dagger.hilt.android)


    //ss
    implementation("androidx.core:core-splashscreen:1.0.0")
    //navigate
    implementation ("androidx.navigation:navigation-compose:2.8.4")
        //font
    dependencies {
        implementation ("androidx.compose.ui:ui-text-google-fonts:+")
        implementation ("androidx.compose.ui:ui-text:1.5.0")  // Make sure the version is correct
    }
    //gs
    implementation ("com.google.android.gms:play-services-auth:20.4.1")
    //firestore
    // Firebase Authentication dependency
    implementation("com.google.firebase:firebase-auth-ktx:22.1.1")
    implementation ("com.google.firebase:firebase-database:20.2.1")
   // implementation "com.google.firebase:firebase-database-ktx:20.2.3"
    implementation ("com.google.firebase:firebase-storage-ktx:20.2.2")


    dependencies {
        // Import the BoM for the Firebase platform
        implementation(platform("com.google.firebase:firebase-bom:33.5.1"))

        // Declare the dependency for the Cloud Firestore library
        // When using the BoM, you don't specify versions in Firebase library dependencies
        implementation("com.google.firebase:firebase-firestore")
    }
    //live data
    implementation ("androidx.compose.runtime:runtime-livedata:1.5.1")
// Image loading (Coil for Jetpack Compose)
    implementation ("io.coil-kt:coil-compose:2.4.0")
    // Activity and Fragment for Compose integration
    implementation ("androidx.activity:activity-compose:1.8.0")
    implementation ("androidx.fragment:fragment-ktx:1.6.1")
    implementation ("com.google.accompanist:accompanist-pager:0.31.5-beta")
    implementation( "com.google.accompanist:accompanist-pager-indicators:0.31.5-beta")

    // AutoImageSlider
   ////image silder

    //gson
    implementation ("com.google.code.gson:gson:2.10.1")

    //room db
    implementation ("androidx.room:room-runtime:2.5.0")
    kapt ("androidx.room:room-compiler:2.5.0")
    implementation ("androidx.room:room-ktx:2.5.2") // Kotlin extensions for Room

    // JSON Handling
    implementation("org.json:json:20230227")

    // Timber for logging
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Volley for network requests
    implementation("com.android.volley:volley:1.2.1")

    // Activity and Fragment for Compose integration
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation("androidx.fragment:fragment-ktx:1.6.1")

    // Accompanist
    implementation("com.google.accompanist:accompanist-pager:0.31.5-beta")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.31.5-beta")
    implementation("com.google.firebase:firebase-messaging")
    //
    implementation ("com.google.auth:google-auth-library-oauth2-http:1.19.0")

    //api using for fcm

    implementation ("com.google.auth:google-auth-library-oauth2-http:1.15.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation ("com.google.firebase:firebase-messaging:23.1.2")
    /*



    implementation("com.github.ZEGOCLOUD:zego_uikit_prebuilt_call_android:+")
    implementation("com.guolindev.permissionx:permissionx:1.8.0")

     */
    //video call waring
    //implementation("com.github.ZEGOCLOUD:zego_uikit_prebuilt_call_android:1.0.0")

    //implementation("com.github.denzcoskun:ImageSlideshow:1.0.0")
}