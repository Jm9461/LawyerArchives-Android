package com.lawyer_archives.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.databinding.ActivityEditLegalClientBinding
import com.lawyer_archives.models.LegalClient
import com.lawyer_archives.utils.DateConverter
import com.lawyer_archives.utils.XmlManager
import java.util.Calendar
import java.util.Date

class EditLegalClientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditLegalClientBinding
    private var originalClientId: String? = null
    private var currentLegalClient: LegalClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditLegalClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        originalClientId = intent.getStringExtra("clientId")
        if (originalClientId == null) {
            Toast.makeText(this, "شناسه موکل یافت نشد.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        loadLegalClientData()

        binding.etRegistrationDate.setOnClickListener { showDatePickerDialog() }
        binding.btnSaveLegalClient.setOnClickListener { updateLegalClient() }
    }

    /**
     * Loads the existing legal client data from XML and populates the form fields.
     */
    private fun loadLegalClientData() {
        // Load all legal clients and find the one to edit
        val legalClients = XmlManager.loadLegalClients(this)
        currentLegalClient = legalClients.find { it.id == originalClientId }

        currentLegalClient?.let { client ->
            binding.etCompanyName.setText(client.companyName)
            binding.etRegistrationNumber.setText(client.registrationNumber)
            binding.etRegistrationDate.setText(client.registrationDate)
            binding.etLegalNationalId.setText(client.legalNationalId)
            binding.etEconomicCode.setText(client.economicCode)
            binding.etAddress.setText(client.address)
            binding.etPhone.setText(client.phone)
            binding.etMobile.setText(client.phoneNumber) // phoneNumber in model, mobile in layout
            binding.etEmail.setText(client.email)
            binding.etPostalCode.setText(client.postalCode)
            binding.etDescription.setText(client.description)
            binding.etLegalRepresentativeName.setText(client.legalRepresentativeName)
            binding.etRepresentativeNationalId.setText(client.representativeNationalId)
        } ?: run {
            Toast.makeText(this, "موکل حقوقی برای ویرایش یافت نشد.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    /**
     * Displays a date picker dialog for selecting the registration date.
     */
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
                binding.etRegistrationDate.setText(persianDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    /**
     * Updates the legal client data in the XML file.
     */
    private fun updateLegalClient() {
        val companyName = binding.etCompanyName.text.toString().trim()
        val registrationNumber = binding.etRegistrationNumber.text.toString().trim()
        val registrationDate = binding.etRegistrationDate.text.toString().trim()
        val legalNationalId = binding.etLegalNationalId.text.toString().trim()
        val economicCode = binding.etEconomicCode.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val mobile = binding.etMobile.text.toString().trim() // This maps to phoneNumber in model
        val email = binding.etEmail.text.toString().trim()
        val postalCode = binding.etPostalCode.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()
        val legalRepresentativeName = binding.etLegalRepresentativeName.text.toString().trim()
        val representativeNationalId = binding.etRepresentativeNationalId.text.toString().trim()

        // Basic validation
        if (companyName.isEmpty() || legalNationalId.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "لطفاً فیلدهای اجباری (نام شرکت/سازمان، شناسه ملی، آدرس، تلفن) را پر کنید.", Toast.LENGTH_LONG).show()
            return
        }

        currentLegalClient?.let { existingClient ->
            val updatedLegalClient = existingClient.copy(
                companyName = companyName,
                registrationNumber = registrationNumber,
                registrationDate = registrationDate,
                legalNationalId = legalNationalId,
                economicCode = economicCode,
                address = address,
                phone = phone,
                phoneNumber = mobile,
                email = email,
                postalCode = postalCode,
                description = description,
                legalRepresentativeName = legalRepresentativeName,
                representativeNationalId = representativeNationalId
                // addedDate remains unchanged
            )

            val legalClients = XmlManager.loadLegalClients(this).toMutableList()
            val index = legalClients.indexOfFirst { it.id == updatedLegalClient.id }
            if (index != -1) {
                legalClients[index] = updatedLegalClient
                XmlManager.saveLegalClients(this, legalClients)
                Toast.makeText(this, "موکل حقوقی با موفقیت ویرایش شد.", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "خطا در ویرایش موکل حقوقی: موکل یافت نشد.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
