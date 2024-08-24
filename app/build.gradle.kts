plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.ahyak"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.ahyak"
        minSdk = 26
        targetSdk = 33
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
    viewBinding {
        true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")
    //sharedpreference
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.github.PhilJay:MPAndroidChart:3.1.0") //chart 라이브러리
    //indicator
    implementation("com.google.android.material:material:1.3.0-alpha03")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0") //retrofit
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //로딩 화면_스켈레톤 구현
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    //Room DB
    implementation("androidx.room:room-runtime:2.6.0")
    implementation("androidx.room:room-ktx:2.4.0")
    annotationProcessor("androidx.room:room-compiler:2.6.0")
    kapt("androidx.room:room-compiler:2.6.0")
    //CameraX
    implementation("androidx.camera:camera-core:1.3.0")
    implementation("androidx.camera:camera-camera2:1.3.0")
    implementation("androidx.camera:camera-lifecycle:1.3.0")
    implementation("androidx.camera:camera-view:1.3.0")
    implementation("androidx.camera:camera-extensions:1.3.0")
    //카메라 프리뷰
    implementation ("androidx.core:core-ktx:1.7.0")
}