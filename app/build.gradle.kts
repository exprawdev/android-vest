
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.androidvest.parental"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.androidvest.parental"
        minSdk = 23
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

            resValue(
                "string",
                "app_name",
                "Android Vest Parent"
            )
        }

        create("child") {
            dimension = "edition"
            applicationId = "com.androidvest.child"
            versionNameSuffix = "-child"

            resValue(
                "string",
                "app_name",
                "Android Vest Child"
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
}
