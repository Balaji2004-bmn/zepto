plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.app.zepto"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.app.Zepto"
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

    // Room Database for Offline Functionality (Task 2)
    implementation("androidx.room:room-runtime:2.4.2")
    annotationProcessor("androidx.room:room-compiler:2.4.2")

    // Gson for SharedPreferences (Task 3)
    implementation("com.google.code.gson:gson:2.8.9")

    // RecyclerView (already included via material)
    implementation("androidx.recyclerview:recyclerview:1.2.1")

    // CardView
    implementation("androidx.cardview:cardview:1.0.0")

    // For notification compatibility
    implementation("androidx.core:core:1.9.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
}