import org.jetbrains.kotlin.storage.CacheResetOnProcessCanceled.enabled

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(Versions.compileSdk)
    buildToolsVersion(Versions.buildTools)

    defaultConfig {
        applicationId = "com.montdeska.android_hacker_news"
        targetSdkVersion(Versions.targetSdk)
        minSdkVersion(Versions.minSdk)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField(
                "String",
                "API_URL",
                "\"https://hn.algolia.com/\""
            )
        }
        getByName("debug") {
            buildConfigField(
                "String",
                "API_URL",
                "\"https://hn.algolia.com/\""
            )
        }
    }
    compileOptions {
        coreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        dataBinding = true
    }
    packagingOptions {
        exclude("META-INF/atomicfu.kotlin_module")
    }

}

dependencies {
    implementation(fileTree("libs") { include(listOf("*.jar")) })
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.0.10")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.2")

    // KTX
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.fragment:fragment-ktx:1.2.5")
    implementation("androidx.room:room-ktx:2.2.5")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${Versions.ktxLifeCycle}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${Versions.ktxLifeCycle}")
    implementation("androidx.lifecycle:lifecycle-reactivestreams-ktx:${Versions.ktxLifeCycle}")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:${Versions.retrofit}")
    implementation("com.squareup.retrofit2:converter-gson:${Versions.retrofit}")

    //Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}")

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.1.0")

    //ViewModel Scope
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    // Room
    implementation("androidx.room:room-runtime:${Versions.roomVersion}")
    kapt("androidx.room:room-compiler:${Versions.roomVersion}")
    implementation("androidx.room:room-ktx:${Versions.roomVersion}")
}