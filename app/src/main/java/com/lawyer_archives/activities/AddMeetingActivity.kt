package com.lawyer_archives.activities

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.lawyer_archives.databinding.ActivityAddMeetingBinding
import com.lawyer_archives.models.Meeting
import com.lawyer_archives.receivers.SessionReminderReceiver // Use the same receiver or create a new one if needed
import com.lawyer_archives.utils.DateConverter
import com.lawyer_archives.utils.XmlManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

class AddMeetingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMeetingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMeetingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDateAndTimePickers()
        binding.btnSaveMeeting.setOnClickListener { saveMeeting() }
    }

    private fun setupDateAndTimePickers() {
        binding.etMeetingDate.setOnClickListener { showDatePickerDialog() }
        binding.etMeetingTime.setOnClickListener { showTimePickerDialog() }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val persianDate = DateConverter.convertGregorianToPersian(Date(selectedYear - 1900, selectedMonth, selectedDay))
                binding.etMeetingDate.setText(persianDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                val time = String.format("%02d:%02d", selectedHour, selectedMinute)
                binding.etMeetingTime.setText(time)
            },
            hour,
            minute,
            true // 24-hour format
        )
        timePickerDialog.show()
    }

    private fun saveMeeting() {
        val title = binding.etTitle.text.toString().trim()
        val clientName = binding.etClientName.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()
        val date = binding.etMeetingDate.text.toString().trim()
        val time = binding.etMeetingTime.text.toString().trim()

        if (title.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "لطفاً عنوان، تاریخ و ساعت را وارد کنید.", Toast.LENGTH_SHORT).show()
            return
        }

        val meeting = Meeting(
            id = UUID.randomUUID().toString(),
            title = title,
            clientName = clientName,
            description = description,
            date = date,
            time = time,
            addedDate = DateConverter.convertGregorianToPersian(Date()) // Use the Persian date
        )

        val meetings = XmlManager.loadMeetings(this).toMutableList()
        meetings.add(meeting)
        XmlManager.saveMeetings(this, meetings)

        setReminder(meeting)

        Toast.makeText(this, "قرار ملاقات ذخیره شد.", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun setReminder(meeting: Meeting) {
        val reminderOptionId = binding.rgReminderOptions.checkedRadioButtonId
        if (reminderOptionId == R.id.rb_no_reminder) {
            return // No reminder selected
        }

         // Convert Persian date and time to Gregorian Calendar
        val calendar = Calendar.getInstance()
        val persianDateParts = meeting.date.split("/")
        val persianYear = persianDateParts[0].toInt()
        val persianMonth = persianDateParts[1].toInt()
        val persianDay = persianDateParts[2].toInt()

        val gregorianDate = DateConverter.convertPersianToGregorian(meeting.date) ?: return // Handle potential conversion error

        val timeParts = meeting.time.split(":")
        calendar.time = gregorianDate
        calendar.set(Calendar.HOUR_OF_DAY, timeParts[0].toInt())
        calendar.set(Calendar.MINUTE, timeParts[1].toInt())
        calendar.set(Calendar.SECOND, 0)

        // Calculate reminder time based on selected option
        when (reminderOptionId) {
            R.id.rb_1_day_before -> calendar.add(Calendar.DAY_OF_YEAR, -1)
            R.id.rb_2_days_before -> calendar.add(Calendar.DAY_OF_YEAR, -2)
            R.id.rb_3_days_before -> calendar.add(Calendar.DAY_OF_YEAR, -3)
        }

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            // Reminder time is in the past, don't set it
            Toast.makeText(this, "زمان یادآوری در گذشته است.", Toast.LENGTH_SHORT).show()
            return
        }

        // Create an Intent to the ReminderBroadcastReceiver
        val intent = Intent(this, SessionReminderReceiver::class.java) // Use the same receiver or create a new one if needed
        intent.putExtra("title", meeting.title)
        intent.putExtra("description", meeting.description)
        intent.putExtra("date", meeting.date)
        intent.putExtra("time", meeting.time)
        intent.putExtra("clientName", meeting.clientName)

        // Create a unique PendingIntent
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            meeting.id.hashCode(), // Use a unique request code based on meeting id
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // Required for Android 12+
        )

        // Get the AlarmManager service
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Set the alarm
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }
}