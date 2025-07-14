package com.lawyer_archives.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.lawyer_archives.R
import com.lawyer_archives.adapters.MainViewPagerAdapter // مطمئن شوید این آداپتر وجود دارد
import com.lawyer_archives.databinding.ActivityDashboardBinding // مطمئن شوید از DashboardBinding استفاده می‌کنید

class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityDashboardBinding
    private var isFabMenuOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar را به عنوان ActionBar برنامه تنظیم کنید
        setSupportActionBar(binding.toolbar)

        setupNavigationDrawer(binding.toolbar)
        setupViewPagerAndTabs()
        setupFloatingActionButtons()

        // نمایش نام وکیل در سربرگ ناوبری
        val headerView = binding.navView.getHeaderView(0)
        val navHeaderName = headerView.findViewById<TextView>(R.id.navHeaderName) // ID TextView در header_nav_drawer.xml
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val profileName = sharedPref.getString("profile_name", "وکیل")
        navHeaderName.text = profileName + " عزیز، خوش آمدید!"

        // تنظیم شنونده برای Navigation View
        binding.navView.setNavigationItemSelectedListener(this)
    }

    private fun setupNavigationDrawer(toolbar: Toolbar) {
        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun setupViewPagerAndTabs() {
        val viewPager: ViewPager2 = binding.contentDashboard.viewPager // فرض می‌کنیم viewPager در content_dashboard.xml است
        val tabLayout: TabLayout = binding.contentDashboard.tabLayout // فرض می‌کنیم tabLayout در content_dashboard.xml است

        val adapter = MainViewPagerAdapter(this) // Context for fragment creation
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "خانه"
                1 -> "موکلین"
                2 -> "پرونده‌ها"
                3 -> "جلسات"
                4 -> "جلسات دادگاه"
                5 -> "وظایف روزانه"
                6 -> "اسناد"
                // می‌توانید تب‌های بیشتری اضافه کنید
                else -> "Tab ${position + 1}"
            }
        }.attach()
    }

    private fun setupFloatingActionButtons() {
        val fabMain: FloatingActionButton = binding.contentDashboard.fabMain
        val fabAddCase: FloatingActionButton = binding.contentDashboard.fabAddCase
        val fabAddClient: FloatingActionButton = binding.contentDashboard.fabAddClient
        val fabAddMeeting: FloatingActionButton = binding.contentDashboard.fabAddMeeting
        val fabAddSession: FloatingActionButton = binding.contentDashboard.fabAddSession
        val fabAddDocument: FloatingActionButton = binding.contentDashboard.fabAddDocument
        val fabAddTask: FloatingActionButton = binding.contentDashboard.fabAddTask

        fabAddCase.hide()
        fabAddClient.hide()
        fabAddMeeting.hide()
        fabAddSession.hide()
        fabAddDocument.hide()
        fabAddTask.hide()

        fabMain.setOnClickListener {
            if (isFabMenuOpen) {
                fabAddCase.hide()
                fabAddClient.hide()
                fabAddMeeting.hide()
                fabAddSession.hide()
                fabAddDocument.hide()
                fabAddTask.hide()
                isFabMenuOpen = false
            } else {
                fabAddCase.show()
                fabAddClient.show()
                fabAddMeeting.show()
                fabAddSession.show()
                fabAddDocument.show()
                fabAddTask.show()
                isFabMenuOpen = true
            }
        }

        fabAddCase.setOnClickListener {
            startActivity(Intent(this, AddCaseActivity::class.java))
            // می‌توانید اینجا fabMenu را ببندید
            fabMain.performClick()
        }
        fabAddClient.setOnClickListener {
            startActivity(Intent(this, AddClientActivity::class.java)) // این به AddClientActivity هدایت می‌کند که انتخابگر است
            fabMain.performClick()
        }
        fabAddMeeting.setOnClickListener {
            startActivity(Intent(this, AddMeetingActivity::class.java))
            fabMain.performClick()
        }
        fabAddSession.setOnClickListener {
            startActivity(Intent(this, AddCourtSessionActivity::class.java))
            fabMain.performClick()
        }
        fabAddDocument.setOnClickListener {
            startActivity(Intent(this, AddDocumentActivity::class.java)) // فرض بر این است که AddDocumentActivity را دارید
            fabMain.performClick()
        }
        fabAddTask.setOnClickListener {
            startActivity(Intent(this, AddDailyTaskActivity::class.java))
            fabMain.performClick()
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // این متد برای آیتم‌های منوی ناوبری Navigation Drawer است
        when (item.itemId) {
            R.id.nav_home -> {
                // این می‌تواند به اولین تب (خانه) برگردد یا یک Activity جدید را شروع کند
                binding.contentDashboard.viewPager.currentItem = 0
            }
            R.id.nav_clients -> {
                binding.contentDashboard.viewPager.currentItem = 1 // تب موکلین
            }
            R.id.nav_cases -> {
                binding.contentDashboard.viewPager.currentItem = 2 // تب پرونده‌ها
            }
            R.id.nav_meetings -> {
                binding.contentDashboard.viewPager.currentItem = 3 // تب جلسات
            }
            R.id.nav_sessions -> {
                binding.contentDashboard.viewPager.currentItem = 4 // تب جلسات دادگاه
            }
            R.id.nav_daily_tasks -> {
                binding.contentDashboard.viewPager.currentItem = 5 // تب وظایف روزانه
            }
            R.id.nav_documents -> {
                binding.contentDashboard.viewPager.currentItem = 6 // تب اسناد
            }
            R.id.nav_my_profile -> {
                startActivity(Intent(this, MyProfileActivity::class.java))
            }
            R.id.nav_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
            R.id.nav_useful_links -> {
                startActivity(Intent(this, UsefulLinksActivity::class.java))
            }
            // Add other navigation items here
            R.id.nav_logout -> {
                // پاک کردن اطلاعات ورود و بازگشت به صفحه Login
                val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putBoolean("is_logged_in", false)
                    apply()
                }
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // حذف تمام Activityهای قبلی
                startActivity(intent)
                finish()
            }
            else -> {
                Toast.makeText(this, "آیتم ${item.title} کلیک شد.", Toast.LENGTH_SHORT).show()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}