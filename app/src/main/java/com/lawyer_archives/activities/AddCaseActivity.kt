// LawyerArchives/app/src/main/java/com/lawyer_archives/activities/AddCaseActivity.kt
package com.lawyer_archives.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.databinding.ActivityAddCaseBinding
import com.lawyer_archives.models.Case
import com.lawyer_archives.models.ClientEntry
import com.lawyer_archives.models.LegalClient
import com.lawyer_archives.models.RealClient
import com.lawyer_archives.utils.DateConverter
import com.lawyer_archives.utils.XmlManager
import java.util.Calendar
import java.util.Date

class AddCaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCaseBinding

    private val caseTitles = arrayOf(
        "مطالبه وجه", "خلع ید", "تصرف عدوانی", "الزام به تنظیم سند رسمی",
        "فسخ قرارداد", "ابطال رای داور", "مطالبه خسارت", "طلاق", "اثبات نسب",
        "اعسار", "فک رهن", "ابطال سند", "مطالبه مهریه", "اجرت المثل",
        "تقسیم ترکه", "اجرای حکم"
    )
    private val clientRoles = arrayOf(
        "خواهان", "خوانده", "شاکی", "متهم", "تجدیدنظرخواه", "تجدیدنظرخوانده",
        "فرجام‌خواه", "فرجام‌خوانده", "ثالث", "خواهان تقابل", "خوانده تقابل"
    )
    private val caseStatuses = arrayOf(
        "در حال رسیدگی", "مختومه", "بایگانی", "در حال اجرا"
    )
    private val caseProcesses = arrayOf(
        "تجدیدنظر", "فرجام‌خواهی", "واخواهی", "اعاده دادرسی", "اعتراض ثالث",
        "مطالبه وجه", "اعسار", "طلاق", "اثبات نسب", "خلع ید", "تصرف عدوانی",
        "الزام به تنظیم سند رسمی", "فسخ قرارداد", "ابطال رای داور", "مطالبه خسارت"
    )
    private val cityJudiciaries = arrayOf(
        "تهران", "مشهد", "اصفهان", "شیراز", "تبریز", "اهواز", "کرمان", "یزد", "رشت", "ساری",
        "کرمانشاه", "همدان", "ارومیه", "بندرعباس", "قزوین", "زاهدان", "گرگان", "سنندج",
        "خرم‌آباد", "اراک", "اردبیل", "ایلام", "بجنورد", "بیرجند", "شهرکرد", "سمنان", "یاسوج"
    )
    private val courtLevelAndTypes = arrayOf(
        "بدوی", "تجدید نظر", "دیوان عالی", "دادگاه انقلاب", "دادگاه کیفری",
        "دادگاه خانواده", "شورای حل اختلاف", "دادگاه نظامی", "دادگاه ویژه روحانیت"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
        // Set current date as default for formation date
        binding.etFormationDate.setText(DateConverter.convertGregorianToPersian(Date()))
    }

    private fun setupClickListeners() {
        binding.etCaseTitle.setOnClickListener { showCaseTitleDialog() }
        binding.etFormationDate.setOnClickListener { showDatePickerDialog() }
        binding.etClientName.setOnClickListener { showClientSelectionDialog() }
        binding.etClientRole.setOnClickListener { showClientRoleDialog() }
        binding.etCaseStatus.setOnClickListener { showCaseStatusDialog() }
        binding.etCaseProcess.setOnClickListener { showCaseProcessDialog() }
        binding.etCityJudiciary.setOnClickListener { showCityJudiciaryDialog() }
        binding.etCourtLevelType.setOnClickListener { showCourtLevelTypeDialog() }

        binding.btnSaveCase.setOnClickListener { saveCase() }
    }

    private fun showCaseTitleDialog() {
        AlertDialog.Builder(this)
            .setTitle("انتخاب عنوان پرونده")
            .setItems(caseTitles) { dialog, which ->
                binding.etCaseTitle.setText(caseTitles[which])
                dialog.dismiss()
            }
            .show()
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
                binding.etFormationDate.setText(persianDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun showClientSelectionDialog() {
        val realClients = XmlManager.loadRealClients(this)
        val legalClients = XmlManager.loadLegalClients(this)
        val allClients = (realClients + legalClients).toMutableList()

        if (allClients.isEmpty()) {
            Toast.makeText(this, "هنوز موکلی ثبت نشده است. ابتدا یک موکل اضافه کنید.", Toast.LENGTH_LONG).show()
            return
        }

        val clientNames = allClients.map { it.name }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("انتخاب موکل")
            .setItems(clientNames) { dialog, which ->
                binding.etClientName.setText(clientNames[which])
                dialog.dismiss()
            }
            .show()
    }

    private fun showClientRoleDialog() {
        AlertDialog.Builder(this)
            .setTitle("انتخاب سمت موکل")
            .setItems(clientRoles) { dialog, which ->
                binding.etClientRole.setText(clientRoles[which])
                dialog.dismiss()
            }
            .show()
    }

    private fun showCaseStatusDialog() {
        AlertDialog.Builder(this)
            .setTitle("انتخاب وضعیت پرونده")
            .setItems(caseStatuses) { dialog, which ->
                binding.etCaseStatus.setText(caseStatuses[which])
                dialog.dismiss()
            }
            .show()
    }

    private fun showCaseProcessDialog() {
        AlertDialog.Builder(this)
            .setTitle("انتخاب روند پرونده")
            .setItems(caseProcesses) { dialog, which ->
                binding.etCaseProcess.setText(caseProcesses[which])
                dialog.dismiss()
            }
            .show()
    }

    private fun showCityJudiciaryDialog() {
        AlertDialog.Builder(this)
            .setTitle("انتخاب دادگستری شهرستان")
            .setItems(cityJudiciaries) { dialog, which ->
                binding.etCityJudiciary.setText(cityJudiciaries[which])
                dialog.dismiss()
            }
            .show()
    }

    private fun showCourtLevelTypeDialog() {
        AlertDialog.Builder(this)
            .setTitle("انتخاب درجه و نوع دادگاه")
            .setItems(courtLevelAndTypes) { dialog, which ->
                binding.etCourtLevelType.setText(courtLevelAndTypes[which])
                dialog.dismiss()
            }
            .show()
    }

    private fun saveCase() {
        val title = binding.etCaseTitle.text.toString().trim()
        val formationDate = binding.etFormationDate.text.toString().trim()
        val clientName = binding.etClientName.text.toString().trim()
        val clientRole = binding.etClientRole.text.toString().trim()
        val caseSubject = binding.etCaseSubject.text.toString().trim()
        val status = binding.etCaseStatus.text.toString().trim()
        val process = binding.etCaseProcess.text.toString().trim()
        val caseNumber = binding.etCaseNumber.text.toString().trim()
        val archiveNumber = binding.etArchiveNumber.text.toString().trim()
        val cityJudiciary = binding.etCityJudiciary.text.toString().trim()
        val courtLevelAndType = binding.etCourtLevelType.text.toString().trim()
        val opponentInfo = binding.etOpponentInfo.text.toString().trim()
        val powerOfAttorneyNumber = binding.etPowerOfAttorneyNumber.text.toString().trim()
        val addedDate = DateConverter.convertGregorianToPersian(Date()) // Capture current date as added date

        // Basic validation
        if (title.isEmpty() || formationDate.isEmpty() || clientName.isEmpty() || clientRole.isEmpty() ||
            caseSubject.isEmpty() || status.isEmpty() || process.isEmpty() || caseNumber.isEmpty() ||
            archiveNumber.isEmpty() || cityJudiciary.isEmpty() || courtLevelAndType.isEmpty() ||
            opponentInfo.isEmpty() || powerOfAttorneyNumber.isEmpty()) {
            Toast.makeText(this, "لطفاً تمام فیلدهای اجباری را پر کنید.", Toast.LENGTH_LONG).show()
            return
        }

        val newCase = Case(
            title = title,
            formationDate = formationDate,
            clientName = clientName,
            clientRole = clientRole,
            caseSubject = caseSubject,
            status = status,
            process = process,
            caseNumber = caseNumber,
            archiveNumber = archiveNumber,
            cityJudiciary = cityJudiciary,
            courtLevelAndType = courtLevelAndType,
            opponentInfo = opponentInfo,
            powerOfAttorneyNumber = powerOfAttorneyNumber,
            addedDate = addedDate
        )

        val cases = XmlManager.loadCases(this).toMutableList()
        cases.add(newCase)
        XmlManager.saveCases(this, cases)

        Toast.makeText(this, "پرونده با موفقیت ذخیره شد.", Toast.LENGTH_SHORT).show()
        finish()
    }
}