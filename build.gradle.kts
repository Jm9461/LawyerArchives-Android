    // این بلاک فقط برای تعریف نسخه پلاگین ها است
    plugins {
        // پلاگین اصلی اندروید برای اپلیکیشن ها
        // apply false را اینجا نگه می داریم چون این پلاگین در ماژول app اعمال می شود
        id("com.android.application") version "8.2.2" apply false
        // پلاگین کاتلین برای اندروید
        id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    }

    // تسک clean برای پاکسازی خروجی های بیلد
    tasks.register("clean") {
        delete(rootProject.layout.buildDirectory)
    }
    