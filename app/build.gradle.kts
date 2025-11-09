plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.app.zepto"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.app.zepto"  // Fixed: lowercase package name
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Add this for Room schema export (fixes the warning)
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas".toString(),
                    "room.incremental" to "true"
                )
            }
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // Add build features for view binding
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Firebase for Push Notifications (Task 6)
    implementation("com.google.firebase:firebase-messaging:23.1.0")
    implementation("com.google.firebase:firebase-analytics:21.2.0")

    // Firebase BoM for version management
    implementation(platform("com.google.firebase:firebase-bom:32.0.0"))

    // Room Database for Offline Functionality (Task 2)
    implementation("androidx.room:room-runtime:2.6.0")
    annotationProcessor("androidx.room:room-compiler:2.6.0")

    // Room Kotlin Extensions
    implementation("androidx.room:room-ktx:2.6.0")

    // Gson for SharedPreferences (Task 3)
    implementation("com.google.code.gson:gson:2.10.1")

    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // CardView
    implementation("androidx.cardview:cardview:1.0.0")

    // For notification compatibility
    implementation("androidx.core:core:1.12.0")
    implementation("androidx.core:core-ktx:1.12.0")

    // Navigation Components
    implementation("androidx.navigation:navigation-fragment:2.7.4")
    implementation("androidx.navigation:navigation-ui:2.7.4")

    // Lifecycle components
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata:2.7.0")

    // Image Loading
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    // WorkManager for background tasks
    implementation("androidx.work:work-runtime:2.9.0")
    implementation("androidx.core:core:1.9.0")

    // For better SharedPreferences
    implementation("androidx.preference:preference:1.2.1")
}