// LawyerArchives/build.gradle.kts (Root Project)
plugins {
    // These plugins are applied to the entire project (all modules)
    // and their versions are specified here.
    id("com.android.application") version "8.7.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}

// این بلوک‌ها باید حذف شوند زیرا RepositoriesMode.FAIL_ON_PROJECT_REPOS فعال است
// و مخازن باید در settings.gradle.kts تعریف شوند.

// allprojects {
//     repositories {
//         google()
//         mavenCentral()
//         // اگر مخازن دیگری (مانند JitPack.io یا مخازن محلی) دارید، می‌توانید اینجا اضافه کنید
//     }
// }

// buildscript {
//     repositories {
//         google()
//         mavenCentral()
//     }
//     dependencies {
//         // Dependencies for the build script itself (e.g., Android Gradle Plugin)
//         // classpath("com.android.tools.build:gradle:8.7.2") // مثال: اگر اینجا نیاز به تعریف داشتید
//     }
// }