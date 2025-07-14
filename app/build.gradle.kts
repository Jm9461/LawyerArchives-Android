// LawyerArchives/app/build.gradle.kts
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.lawyer_archives"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.lawyer_archives"
        minSdk = 23
        targetSdk = 35
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

    buildFeatures {
        viewBinding = true
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // AndroidX Core KTX
    implementation("androidx.core:core-ktx:1.13.1")
    // AppCompat
    implementation("androidx.appcompat:appcompat:1.7.0")
    // Material Design
    implementation("com.google.android.material:material:1.12.0")
    // ConstraintLayout
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2") // نسخه به روز شده
    // Activity KTX
    implementation("androidx.activity:activity-ktx:1.9.0") // نسخه به روز شده
    // Fragment KTX
    implementation("androidx.fragment:fragment-ktx:1.8.0") // نسخه به روز شده
    // CardView
    implementation("androidx.cardview:cardview:1.0.0")

    // **** کتابخانه جدید تقویم شمسی از Maven Central ****
    implementation("com.github.babak-pc:PersianCalender:1.3.3")

    // وابستگی های تست
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
