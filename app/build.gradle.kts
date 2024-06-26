import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.iscoding.cryptoexample"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.iscoding.cryptoexample"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
       val  properties = Properties()
//        properties.load(project.rootProject.file("local.properties").inputStream())
        properties.load(project.rootProject.file("keys.properties").inputStream())
        buildConfigField("String","API_KEY", "\"${properties.getProperty("API_KEY")}\"")

        // for meinfest place holder
//        val mapsKeyFile = project.rootProject.file("mapskey.properties")
//        val properties = Properties()
//        properties.load(mapsKeyFile.inputStream())
//
////fetch the map key
//        val apiKey = properties.getProperty("MAPS_API_KEY") ?: ""
//
////inject the key dynamically into the manifest
//        manifestPlaceholders["GOOGLE_KEY"] = apiKey
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
        buildConfig = true
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //security
    implementation(libs.androidx.security.crypto)
    implementation(libs.androidx.security.state)

    // data store
        // preferences
    implementation ("androidx.datastore:datastore-preferences:1.0.0-alpha04")
    implementation ("androidx.datastore:datastore:1.0.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")


}
