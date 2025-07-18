// مدیریت پلاگین ها: مخازنی که Gradle برای دانلود پلاگین ها جستجو می کند
pluginManagement {
    repositories {
        google() // مخزن گوگل برای پلاگین های اندروید
        mavenCentral() // مخزن مرکزی Maven
        gradlePluginPortal() // پورتال پلاگین های Gradle
    }
}

// مدیریت وابستگی ها: مخازنی که Gradle برای دانلود کتابخانه ها جستجو می کند
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google() // مخزن گوگل برای کتابخانه های اندروید
        mavenCentral() // مخزن مرکزی Maven
        maven { url = uri("https://jitpack.io") } // برای کتابخانه هایی مانند PersianCalender
    }
}

// نام پروژه اصلی شما
rootProject.name = "LawyerArchives"

// ماژول های موجود در پروژه شما
// فقط ماژول 'app' را شامل می شود، مگر اینکه ماژول های دیگری داشته باشید
include(":app")
// اگر ماژول های دیگری مانند 'wear' یا 'gradlePlugins' دارید، آنها را اینجا اضافه کنید:
// include(":wear")
// includeBuild("gradlePlugins")
