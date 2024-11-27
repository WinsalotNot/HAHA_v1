plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.HAHA"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.HAHA"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true // Correct placement of buildConfig
    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", "\"http://views-crude.gl.at.ply.gg:59304/api/payment/callback/\"")
            buildConfigField("String", "CLIENT_KEY", "\"SB-Mid-client-QrtMd37smh-W08Ry\"")
        }
        release {
            buildConfigField("String", "BASE_URL", "\"http://views-crude.gl.at.ply.gg:59304/api/payment/callback/\"")
            buildConfigField("String", "CLIENT_KEY", "\"SB-Mid-client-QrtMd37smh-W08Ry\"")
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Retrofit and Gson Converter
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Testing Dependencies
    testImplementation(libs.junit) // JUnit for unit testing
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //For Testing
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Payment Gateway = Midtrans
    implementation(libs.uikit)

    // Import the BoM for the Firebase platform
    implementation(platform(libs.firebase.bom))

    implementation(libs.firebase.analytics)

    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation(libs.firebase.auth)

    // Also add the dependency for the Google Play services library and specify its version
    implementation(libs.play.services.auth)
}
