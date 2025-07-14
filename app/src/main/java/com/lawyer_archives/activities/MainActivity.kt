package com.lawyer_archives.activities

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
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
import com.lawyer_archives.adapters.MainViewPagerAdapter
import com.lawyer_archives.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private var isFabMenuOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        setupNavigationDrawer(binding.toolbar)
        setupViewPagerAndTabs()
        setupFloatingActionButtons()

        // می‌توانید نام وکیل را در سربرگ منوی کشویی تنظیم کنید
        val headerView = binding.navView.getHeaderView(0)
        // val lawyerNameTextView: TextView = headerView.findViewById(R.id.textView)
        // val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        // lawyerNameTextView.text = sharedPref.getString("profile_name", "نام وکیل")
    }

    private fun setupNavigationDrawer(toolbar: Toolbar) {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                // Ignore
            }
            override fun onDrawerOpened(drawerView: View) {
                // Ignore
            }
            override fun onDrawerClosed(drawerView: View) {
                // Ignore
            }
            override fun onDrawerStateChanged(newState: Int) {
                // Ignore
            }
        })
        toggle.isDrawerIndicatorEnabled = false
        toolbar.setNavigationIcon(R.drawable.ic_menu_right) // آیکون سه خط در سمت راست
        toolbar.setNavigationOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END)
            } else {
                drawerLayout.openDrawer(GravityCompat.END)
            }
        }
    }

    private fun setupViewPagerAndTabs() {
        val viewPager: ViewPager2 = binding.viewPager
        val tabLayout: TabLayout = binding.tabLayout

        val adapter = MainViewPagerAdapter(this)
        viewPager.adapter = adapter

        // نام تب‌ها را به 15 عدد افزایش دادیم
        val tabTitles = arrayOf(
            "جلسات",
            "موکلین",
            "پرونده‌ها",
            "مدیریت دفتر وکالت",
            "محاسبات قضایی",
            "محاسبات اسنادی",
            "محاسبات عمومی",
            "مواعد قانونی",
            "آراء وحدت رویه",
            "نظریه های مشورتی",
            "رویه های قضایی",
            "بانک قوانین",
            "نیازهای حقوقی",
            "مکان یاب قضایی",
            "خدمات ثبتی"
        )

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    private fun setupFloatingActionButtons() {
        binding.fabMain.setOnClickListener {
            if (isFabMenuOpen) {
                hideFabMenu()
            } else {
                showFabMenu()
            }
        }

        binding.fabAddSession.setOnClickListener {
            // Intent به اکتیویتی اضافه کردن جلسه دادرسی جدید
            // startActivity(Intent(this, AddCourtSessionActivity::class.java))
            Toast.makeText(this, "افزودن جلسه دادرسی جدید", Toast.LENGTH_SHORT).show()
            hideFabMenu()
        }

        binding.fabAddMeeting.setOnClickListener {
            // Intent به اکتیویتی اضافه کردن قرار ملاقات جدید
            // startActivity(Intent(this, AddMeetingActivity::class.java))
            Toast.makeText(this, "افزودن قرار ملاقات جدید", Toast.LENGTH_SHORT).show()
            hideFabMenu()
        }
    }

    private fun showFabMenu() {
        isFabMenuOpen = true
        binding.fabAddSession.show()
        binding.fabAddMeeting.show()
        binding.textAddSession.visibility = View.VISIBLE
        binding.textAddMeeting.visibility = View.VISIBLE
    }

    private fun hideFabMenu() {
        isFabMenuOpen = false
        binding.fabAddSession.hide()
        binding.fabAddMeeting.hide()
        binding.textAddSession.visibility = View.GONE
        binding.textAddMeeting.visibility = View.GONE
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val viewPager: ViewPager2 = binding.viewPager
        val tabLayout: TabLayout = binding.tabLayout

        when (item.itemId) {
            R.id.nav_profile -> {
                Toast.makeText(this, "پروفایل من", Toast.LENGTH_SHORT).show()
                // startActivity(Intent(this, ProfileActivity::class.java))
            }
            R.id.nav_backup_restore -> {
                Toast.makeText(this, "پشتیبان‌گیری و بازیابی", Toast.LENGTH_SHORT).show()
                // startActivity(Intent(this, BackupRestoreActivity::class.java))
            }
            R.id.nav_reminders -> {
                Toast.makeText(this, "یادآورها", Toast.LENGTH_SHORT).show()
                // startActivity(Intent(this, RemindersActivity::class.java))
            }
            R.id.nav_settings -> {
                Toast.makeText(this, "تنظیمات", Toast.LENGTH_SHORT).show()
                // startActivity(Intent(this, SettingsActivity::class.java))
            }
            R.id.nav_meetings -> {
                viewPager.currentItem = 0 // تب جلسات
                tabLayout.getTabAt(0)?.select()
            }
            R.id.nav_clients -> {
                viewPager.currentItem = 1 // تب موکلین
                tabLayout.getTabAt(1)?.select()
            }
            R.id.nav_cases -> {
                viewPager.currentItem = 2 // تب پرونده‌ها
                tabLayout.getTabAt(2)?.select()
            }
            R.id.nav_office_management -> {
                viewPager.currentItem = 3 // تب مدیریت دفتر وکالت
                tabLayout.getTabAt(3)?.select()
            }
            R.id.nav_judicial_calculations -> {
                viewPager.currentItem = 4 // تب محاسبات قضایی
                tabLayout.getTabAt(4)?.select()
            }
            R.id.nav_document_calculations -> {
                viewPager.currentItem = 5 // تب محاسبات اسنادی
                tabLayout.getTabAt(5)?.select()
            }
            R.id.nav_public_calculations -> {
                viewPager.currentItem = 6 // تب محاسبات عمومی
                tabLayout.getTabAt(6)?.select()
            }
            R.id.nav_legal_deadlines -> {
                viewPager.currentItem = 7 // تب مواعد قانونی
                tabLayout.getTabAt(7)?.select()
            }
            R.id.nav_unanimity_of_procedure -> {
                viewPager.currentItem = 8 // تب آراء وحدت رویه
                tabLayout.getTabAt(8)?.select()
            }
            R.id.nav_consultative_opinions -> {
                viewPager.currentItem = 9 // تب نظریه های مشورتی
                tabLayout.getTabAt(9)?.select()
            }
            R.id.nav_judicial_procedures -> {
                viewPager.currentItem = 10 // تب رویه های قضایی
                tabLayout.getTabAt(10)?.select()
            }
            R.id.nav_law_bank -> {
                viewPager.currentItem = 11 // تب بانک قوانین
                tabLayout.getTabAt(11)?.select()
            }
            R.id.nav_legal_needs -> {
                viewPager.currentItem = 12 // تب نیازهای حقوقی
                tabLayout.getTabAt(12)?.select()
            }
            R.id.nav_judicial_locator -> {
                viewPager.currentItem = 13 // تب مکان یاب قضایی
                tabLayout.getTabAt(13)?.select()
            }
            R.id.nav_registration_services -> {
                viewPager.currentItem = 14 // تب خدمات ثبتی
                tabLayout.getTabAt(14)?.select()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.END) // بستن منوی کشویی
        return true
    }
}