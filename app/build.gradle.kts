
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.androidvest.parental"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.androidvest.parental"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    flavorDimensions += "edition"

    productFlavors {
        create("parent") {
            dimension = "edition"
            applicationId = "com.androidvest.parent"
            versionNameSuffix = "-parent"

            resValue("string", "app_name", "Android Vest Parent")
        }

        create("child") {
            dimension = "edition"
            applicationId = "com.androidvest.child"
            versionNameSuffix = "-child"

            resValue("string", "app_name", "Android Vest Child")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:35.2.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
}
