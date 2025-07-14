// LawyerArchives/app/src/main/java/com/lawyer_archives/activities/AddDailyTaskActivity.kt
package com.lawyer_archives.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.databinding.ActivityAddDailyTaskBinding
import com.lawyer_archives.models.DailyTask
import com.lawyer_archives.utils.DateConverter
import com.lawyer_archives.utils.XmlManager
import java.util.Calendar
import java.util.Date

class AddDailyTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddDailyTaskBinding
    private var tabPosition: Int = 0 // 0: Daily, 1: Client/Case

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDailyTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tabPosition = intent.getIntExtra("tabPosition", 0) // Get tab info

        binding.etTaskDueDate.setOnClickListener { showDatePickerDialog() }
        binding.btnSaveTask.setOnClickListener { saveTask() }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val gregorianDate = Date(selectedYear - 1900, selectedMonth, selectedDay)
                val persianDate = DateConverter.convertGregorianToPersian(gregorianDate)
                binding.etTaskDueDate.setText(persianDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun saveTask() {
        val title = binding.etTaskTitle.text.toString().trim()
        val description = binding.etTaskDescription.text.toString().trim()
        val dueDate = binding.etTaskDueDate.text.toString().trim()

        if (title.isEmpty() || description.isEmpty() || dueDate.isEmpty()) {
            Toast.makeText(this, "لطفاً تمام فیلدها را پر کنید.", Toast.LENGTH_SHORT).show()
            return
        }

        val newTask = DailyTask(
            title = title,
            description = description,
            dueDate = dueDate,
            relatedClientOrCase = null //  For now, not handling client/case linking
        )

        val tasks = XmlManager.loadDailyTasks(this).toMutableList()
        tasks.add(newTask)
        XmlManager.saveDailyTasks(this, tasks)

        Toast.makeText(this, "یادداشت ذخیره شد.", Toast.LENGTH_SHORT).show()
        finish()
    }
}