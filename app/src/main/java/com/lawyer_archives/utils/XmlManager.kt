package com.lawyer_archives.utils

import android.content.Context
import com.lawyer_archives.models.Case
import com.lawyer_archives.models.ClientEntry
import com.lawyer_archives.models.CourtSession
import com.lawyer_archives.models.DailyTask
import com.lawyer_archives.models.Document
import com.lawyer_archives.models.LegalClient
import com.lawyer_archives.models.Meeting
import com.lawyer_archives.models.RealClient
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import org.xmlpull.v1.XmlSerializer
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.StringWriter
import java.util.UUID

object XmlManager {

    // --- File Names ---
    private const val CASES_FILE_NAME = "cases.xml"
    private const val REAL_CLIENTS_FILE_NAME = "real_clients.xml"
    private const val LEGAL_CLIENTS_FILE_NAME = "legal_clients.xml"
    const val SESSIONS_FILE_NAME = "sessions.xml"
    const val MEETINGS_FILE_NAME = "meetings.xml"
    private const val DOCUMENTS_FILE_NAME = "documents.xml"
    private const val DAILY_TASKS_FILE_NAME = "daily_tasks.xml"


    /**
     * Creates an empty XML file if it does not exist.
     * This is useful to ensure file existence before attempting to read from it.
     * @param context The application context.
     * @param fileName The name of the XML file to create.
     * @param rootTag The root tag for the XML file (e.g., "Cases", "RealClients").
     */
    fun createEmptyXmlFile(context: Context, fileName: String, rootTag: String) {
        val file = File(context.filesDir, fileName)
        if (!file.exists()) {
            FileOutputStream(file).use { fos ->
                val xmlSerializer = XmlPullParserFactory.newInstance().newSerializer()
                xmlSerializer.setOutput(fos, "UTF-8")
                xmlSerializer.startDocument("UTF-8", true)
                xmlSerializer.startTag("", rootTag)
                xmlSerializer.endTag("", rootTag)
                xmlSerializer.endDocument()
            }
        }
    }

    // --- Cases ---
    fun saveCases(context: Context, cases: List<Case>) {
        createEmptyXmlFile(context, CASES_FILE_NAME, "Cases")
        val file = File(context.filesDir, CASES_FILE_NAME)
        FileOutputStream(file).use { fileOutputStream ->
            val xmlSerializer = XmlPullParserFactory.newInstance().newSerializer()
            val writer = StringWriter()

            xmlSerializer.setOutput(writer)
            xmlSerializer.startDocument("UTF-8", true)
            xmlSerializer.startTag("", "Cases")

            for (case in cases) {
                xmlSerializer.startTag("", "Case")
                xmlSerializer.attribute("", "id", case.id)

                xmlSerializer.startTag("", "Title")
                xmlSerializer.text(case.title)
                xmlSerializer.endTag("", "Title")

                xmlSerializer.startTag("", "FormationDate")
                xmlSerializer.text(case.formationDate)
                xmlSerializer.endTag("", "FormationDate")

                xmlSerializer.startTag("", "ClientName")
                xmlSerializer.text(case.clientName)
                xmlSerializer.endTag("", "ClientName")

                xmlSerializer.startTag("", "ClientRole")
                xmlSerializer.text(case.clientRole)
                xmlSerializer.endTag("", "ClientRole")

                xmlSerializer.startTag("", "CaseSubject")
                xmlSerializer.text(case.caseSubject)
                xmlSerializer.endTag("", "CaseSubject")

                xmlSerializer.startTag("", "Status")
                xmlSerializer.text(case.status)
                xmlSerializer.endTag("", "Status")

                xmlSerializer.startTag("", "Process")
                xmlSerializer.text(case.process)
                xmlSerializer.endTag("", "Process")

                xmlSerializer.startTag("", "CaseNumber")
                xmlSerializer.text(case.caseNumber)
                xmlSerializer.endTag("", "CaseNumber")

                xmlSerializer.startTag("", "ArchiveNumber")
                xmlSerializer.text(case.archiveNumber)
                xmlSerializer.endTag("", "ArchiveNumber")

                xmlSerializer.startTag("", "CityJudiciary")
                xmlSerializer.text(case.cityJudiciary)
                xmlSerializer.endTag("", "CityJudiciary")

                xmlSerializer.startTag("", "CourtLevelAndType")
                xmlSerializer.text(case.courtLevelAndType)
                xmlSerializer.endTag("", "CourtLevelAndType")

                xmlSerializer.startTag("", "OpponentInfo")
                xmlSerializer.text(case.opponentInfo)
                xmlSerializer.endTag("", "OpponentInfo")

                xmlSerializer.startTag("", "PowerOfAttorneyNumber")
                xmlSerializer.text(case.powerOfAttorneyNumber)
                xmlSerializer.endTag("", "PowerOfAttorneyNumber")

                xmlSerializer.startTag("", "AddedDate")
                xmlSerializer.text(case.addedDate)
                xmlSerializer.endTag("", "AddedDate")

                xmlSerializer.endTag("", "Case")
            }
            xmlSerializer.endTag("", "Cases")
            xmlSerializer.endDocument()
            fileOutputStream.write(writer.toString().toByteArray())
        }
    }

    fun loadCases(context: Context): List<Case> {
        createEmptyXmlFile(context, CASES_FILE_NAME, "Cases")
        val cases = mutableListOf<Case>()
        val file = File(context.filesDir, CASES_FILE_NAME)
        if (!file.exists() || file.length() == 0L) {
            return cases // Return empty list if file doesn't exist or is empty
        }

        FileInputStream(file).use { fileInputStream ->
            val parserFactory = XmlPullParserFactory.newInstance()
            val parser = parserFactory.newPullParser()
            parser.setInput(fileInputStream, null)

            var eventType = parser.eventType
            var currentCase: Case? = null
            var text: String?

            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = parser.name
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if (tagName == "Case") {
                            currentCase = Case(id = parser.getAttributeValue("", "id") ?: UUID.randomUUID().toString(),
                                title = "", formationDate = "", clientName = "", clientRole = "",
                                caseSubject = "", status = "", process = "", caseNumber = "",
                                archiveNumber = "", cityJudiciary = "", courtLevelAndType = "",
                                opponentInfo = "", powerOfAttorneyNumber = "", addedDate = "")
                        }
                    }
                    XmlPullParser.TEXT -> text = parser.text
                    XmlPullParser.END_TAG -> {
                        when (tagName) {
                            "Case" -> currentCase?.let { cases.add(it) }
                            "Title" -> currentCase = currentCase?.copy(title = text ?: "")
                            "FormationDate" -> currentCase = currentCase?.copy(formationDate = text ?: "")
                            "ClientName" -> currentCase = currentCase?.copy(clientName = text ?: "")
                            "ClientRole" -> currentCase = currentCase?.copy(clientRole = text ?: "")
                            "CaseSubject" -> currentCase = currentCase?.copy(caseSubject = text ?: "")
                            "Status" -> currentCase = currentCase?.copy(status = text ?: "")
                            "Process" -> currentCase = currentCase?.copy(process = text ?: "")
                            "CaseNumber" -> currentCase = currentCase?.copy(caseNumber = text ?: "")
                            "ArchiveNumber" -> currentCase = currentCase?.copy(archiveNumber = text ?: "")
                            "CityJudiciary" -> currentCase = currentCase?.copy(cityJudiciary = text ?: "")
                            "CourtLevelAndType" -> currentCase = currentCase?.copy(courtLevelAndType = text ?: "")
                            "OpponentInfo" -> currentCase = currentCase?.copy(opponentInfo = text ?: "")
                            "PowerOfAttorneyNumber" -> currentCase = currentCase?.copy(powerOfAttorneyNumber = text ?: "")
                            "AddedDate" -> currentCase = currentCase?.copy(addedDate = text ?: "")
                        }
                    }
                }
                eventType = parser.next()
            }
        }
        return cases
    }


    // --- Real Clients ---
    fun saveRealClients(context: Context, realClients: List<RealClient>) {
        createEmptyXmlFile(context, REAL_CLIENTS_FILE_NAME, "RealClients")
        val file = File(context.filesDir, REAL_CLIENTS_FILE_NAME)
        FileOutputStream(file).use { fileOutputStream ->
            val xmlSerializer = XmlPullParserFactory.newInstance().newSerializer()
            val writer = StringWriter()

            xmlSerializer.setOutput(writer)
            xmlSerializer.startDocument("UTF-8", true)
            xmlSerializer.startTag("", "RealClients")

            for (client in realClients) {
                xmlSerializer.startTag("", "RealClient")
                xmlSerializer.attribute("", "id", client.id)

                xmlSerializer.startTag("", "FullName")
                xmlSerializer.text(client.fullName)
                xmlSerializer.endTag("", "FullName")

                xmlSerializer.startTag("", "FatherName")
                xmlSerializer.text(client.fatherName)
                xmlSerializer.endTag("", "FatherName")

                xmlSerializer.startTag("", "IdCardNumber")
                xmlSerializer.text(client.idCardNumber)
                xmlSerializer.endTag("", "IdCardNumber")

                xmlSerializer.startTag("", "NationalId")
                xmlSerializer.text(client.nationalId)
                xmlSerializer.endTag("", "NationalId")

                xmlSerializer.startTag("", "BirthDate")
                xmlSerializer.text(client.birthDate)
                xmlSerializer.endTag("", "BirthDate")

                xmlSerializer.startTag("", "BirthPlace")
                xmlSerializer.text(client.birthPlace)
                xmlSerializer.endTag("", "BirthPlace")

                xmlSerializer.startTag("", "Address")
                xmlSerializer.text(client.address)
                xmlSerializer.endTag("", "Address")

                xmlSerializer.startTag("", "Phone")
                xmlSerializer.text(client.phone)
                xmlSerializer.endTag("", "Phone")

                xmlSerializer.startTag("", "PhoneNumber") // This is for mobile number
                xmlSerializer.text(client.phoneNumber)
                xmlSerializer.endTag("", "PhoneNumber")

                xmlSerializer.startTag("", "Occupation")
                xmlSerializer.text(client.occupation)
                xmlSerializer.endTag("", "Occupation")

                xmlSerializer.startTag("", "Email")
                xmlSerializer.text(client.email)
                xmlSerializer.endTag("", "Email")

                xmlSerializer.startTag("", "PostalCode")
                xmlSerializer.text(client.postalCode)
                xmlSerializer.endTag("", "PostalCode")

                xmlSerializer.startTag("", "Description")
                xmlSerializer.text(client.description)
                xmlSerializer.endTag("", "Description")

                xmlSerializer.startTag("", "ReferredCasesCount")
                xmlSerializer.text(client.referredCasesCount.toString())
                xmlSerializer.endTag("", "ReferredCasesCount")

                xmlSerializer.startTag("", "AddedDate")
                xmlSerializer.text(client.addedDate)
                xmlSerializer.endTag("", "AddedDate")

                xmlSerializer.endTag("", "RealClient")
            }
            xmlSerializer.endTag("", "RealClients")
            xmlSerializer.endDocument()
            fileOutputStream.write(writer.toString().toByteArray())
        }
    }

    fun loadRealClients(context: Context): List<RealClient> {
        createEmptyXmlFile(context, REAL_CLIENTS_FILE_NAME, "RealClients")
        val realClients = mutableListOf<RealClient>()
        val file = File(context.filesDir, REAL_CLIENTS_FILE_NAME)
        if (!file.exists() || file.length() == 0L) {
            return realClients
        }

        FileInputStream(file).use { fileInputStream ->
            val parserFactory = XmlPullParserFactory.newInstance()
            val parser = parserFactory.newPullParser()
            parser.setInput(fileInputStream, null)

            var eventType = parser.eventType
            var currentClient: RealClient? = null
            var text: String? = ""

            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = parser.name
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if (tagName == "RealClient") {
                            currentClient = RealClient(
                                id = parser.getAttributeValue("", "id") ?: UUID.randomUUID().toString(),
                                fullName = "", fatherName = "", idCardNumber = "", nationalId = "",
                                birthDate = "", birthPlace = "", address = "", phone = "",
                                phoneNumber = "", occupation = "", email = "", postalCode = "",
                                description = "", referredCasesCount = 0, addedDate = ""
                            )
                        }
                    }
                    XmlPullParser.TEXT -> text = parser.text
                    XmlPullParser.END_TAG -> {
                        when (tagName) {
                            "RealClient" -> currentClient?.let { realClients.add(it) }
                            "FullName" -> currentClient = currentClient?.copy(fullName = text ?: "")
                            "FatherName" -> currentClient = currentClient?.copy(fatherName = text ?: "")
                            "IdCardNumber" -> currentClient = currentClient?.copy(idCardNumber = text ?: "")
                            "NationalId" -> currentClient = currentClient?.copy(nationalId = text ?: "")
                            "BirthDate" -> currentClient = currentClient?.copy(birthDate = text ?: "")
                            "BirthPlace" -> currentClient = currentClient?.copy(birthPlace = text ?: "")
                            "Address" -> currentClient = currentClient?.copy(address = text ?: "")
                            "Phone" -> currentClient = currentClient?.copy(phone = text ?: "")
                            "PhoneNumber" -> currentClient = currentClient?.copy(phoneNumber = text ?: "")
                            "Occupation" -> currentClient = currentClient?.copy(occupation = text ?: "")
                            "Email" -> currentClient = currentClient?.copy(email = text ?: "")
                            "PostalCode" -> currentClient = currentClient?.copy(postalCode = text ?: "")
                            "Description" -> currentClient = currentClient?.copy(description = text ?: "")
                            "ReferredCasesCount" -> currentClient = currentClient?.copy(referredCasesCount = (text ?: "0").toInt())
                            "AddedDate" -> currentClient = currentClient?.copy(addedDate = text ?: "")
                        }
                    }
                }
                eventType = parser.next()
            }
        }
        return realClients
    }

    // --- Legal Clients ---
    fun saveLegalClients(context: Context, legalClients: List<LegalClient>) {
        createEmptyXmlFile(context, LEGAL_CLIENTS_FILE_NAME, "LegalClients")
        val file = File(context.filesDir, LEGAL_CLIENTS_FILE_NAME)
        FileOutputStream(file).use { fileOutputStream ->
            val xmlSerializer = XmlPullParserFactory.newInstance().newSerializer()
            val writer = StringWriter()

            xmlSerializer.setOutput(writer)
            xmlSerializer.startDocument("UTF-8", true)
            xmlSerializer.startTag("", "LegalClients")

            for (client in legalClients) {
                xmlSerializer.startTag("", "LegalClient")
                xmlSerializer.attribute("", "id", client.id)

                xmlSerializer.startTag("", "CompanyName")
                xmlSerializer.text(client.companyName)
                xmlSerializer.endTag("", "CompanyName")

                xmlSerializer.startTag("", "RegistrationNumber")
                xmlSerializer.text(client.registrationNumber)
                xmlSerializer.endTag("", "RegistrationNumber")

                xmlSerializer.startTag("", "RegistrationDate")
                xmlSerializer.text(client.registrationDate)
                xmlSerializer.endTag("", "RegistrationDate")

                xmlSerializer.startTag("", "LegalNationalId")
                xmlSerializer.text(client.legalNationalId)
                xmlSerializer.endTag("", "LegalNationalId")

                xmlSerializer.startTag("", "EconomicCode")
                xmlSerializer.text(client.economicCode)
                xmlSerializer.endTag("", "EconomicCode")

                xmlSerializer.startTag("", "Address")
                xmlSerializer.text(client.address)
                xmlSerializer.endTag("", "Address")

                xmlSerializer.startTag("", "Phone")
                xmlSerializer.text(client.phone)
                xmlSerializer.endTag("", "Phone")

                xmlSerializer.startTag("", "PhoneNumber") // This is for mobile number
                xmlSerializer.text(client.phoneNumber)
                xmlSerializer.endTag("", "PhoneNumber")

                xmlSerializer.startTag("", "Email")
                xmlSerializer.text(client.email)
                xmlSerializer.endTag("", "Email")

                xmlSerializer.startTag("", "PostalCode")
                xmlSerializer.text(client.postalCode)
                xmlSerializer.endTag("", "PostalCode")

                xmlSerializer.startTag("", "Description")
                xmlSerializer.text(client.description)
                xmlSerializer.endTag("", "Description")

                xmlSerializer.startTag("", "LegalRepresentativeName")
                xmlSerializer.text(client.legalRepresentativeName)
                xmlSerializer.endTag("", "LegalRepresentativeName")

                xmlSerializer.startTag("", "RepresentativeNationalId")
                xmlSerializer.text(client.representativeNationalId)
                xmlSerializer.endTag("", "RepresentativeNationalId")

                xmlSerializer.startTag("", "ReferredCasesCount")
                xmlSerializer.text(client.referredCasesCount.toString())
                xmlSerializer.endTag("", "ReferredCasesCount")

                xmlSerializer.startTag("", "AddedDate")
                xmlSerializer.text(client.addedDate)
                xmlSerializer.endTag("", "AddedDate")

                xmlSerializer.endTag("", "LegalClient")
            }
            xmlSerializer.endTag("", "LegalClients")
            xmlSerializer.endDocument()
            fileOutputStream.write(writer.toString().toByteArray())
        }
    }

    fun loadLegalClients(context: Context): List<LegalClient> {
        createEmptyXmlFile(context, LEGAL_CLIENTS_FILE_NAME, "LegalClients")
        val legalClients = mutableListOf<LegalClient>()
        val file = File(context.filesDir, LEGAL_CLIENTS_FILE_NAME)
        if (!file.exists() || file.length() == 0L) {
            return legalClients
        }

        FileInputStream(file).use { fileInputStream ->
            val parserFactory = XmlPullParserFactory.newInstance()
            val parser = parserFactory.newPullParser()
            parser.setInput(fileInputStream, null)

            var eventType = parser.eventType
            var currentClient: LegalClient? = null
            var text: String? = ""

            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = parser.name
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if (tagName == "LegalClient") {
                            currentClient = LegalClient(
                                id = parser.getAttributeValue("", "id") ?: UUID.randomUUID().toString(),
                                companyName = "", registrationNumber = "", registrationDate = "",
                                legalNationalId = "", economicCode = "", address = "", phone = "",
                                phoneNumber = "", email = "", postalCode = "", description = "",
                                legalRepresentativeName = "", representativeNationalId = "", referredCasesCount = 0, addedDate = ""
                            )
                        }
                    }
                    XmlPullParser.TEXT -> text = parser.text
                    XmlPullParser.END_TAG -> {
                        when (tagName) {
                            "LegalClient" -> currentClient?.let { legalClients.add(it) }
                            "CompanyName" -> currentClient = currentClient?.copy(companyName = text ?: "")
                            "RegistrationNumber" -> currentClient = currentClient?.copy(registrationNumber = text ?: "")
                            "RegistrationDate" -> currentClient = currentClient?.copy(registrationDate = text ?: "")
                            "LegalNationalId" -> currentClient = currentClient?.copy(legalNationalId = text ?: "")
                            "EconomicCode" -> currentClient = currentClient?.copy(economicCode = text ?: "")
                            "Address" -> currentClient = currentClient?.copy(address = text ?: "")
                            "Phone" -> currentClient = currentClient?.copy(phone = text ?: "")
                            "PhoneNumber" -> currentClient = currentClient?.copy(phoneNumber = text ?: "")
                            "Email" -> currentClient = currentClient?.copy(email = text ?: "")
                            "PostalCode" -> currentClient = currentClient?.copy(postalCode = text ?: "")
                            "Description" -> currentClient = currentClient?.copy(description = text ?: "")
                            "LegalRepresentativeName" -> currentClient = currentClient?.copy(legalRepresentativeName = text ?: "")
                            "RepresentativeNationalId" -> currentClient = currentClient?.copy(representativeNationalId = text ?: "")
                            "ReferredCasesCount" -> currentClient = currentClient?.copy(referredCasesCount = (text ?: "0").toInt())
                            "AddedDate" -> currentClient = currentClient?.copy(addedDate = text ?: "")
                        }
                    }
                }
                eventType = parser.next()
            }
        }
        return legalClients
    }

    /**
     * Loads both real and legal clients and returns them as a combined list of ClientEntry.
     */
    fun loadAllClients(context: Context): List<ClientEntry> {
        val allClients = mutableListOf<ClientEntry>()
        allClients.addAll(loadRealClients(context))
        allClients.addAll(loadLegalClients(context))
        return allClients
    }

    // --- Sessions ---
    fun saveSessions(context: Context, sessions: List<CourtSession>) {
        createEmptyXmlFile(context, SESSIONS_FILE_NAME, "Sessions")
        val file = File(context.filesDir, SESSIONS_FILE_NAME)
        FileOutputStream(file).use { fileOutputStream ->
            val xmlSerializer = XmlPullParserFactory.newInstance().newSerializer()
            val writer = StringWriter()

            xmlSerializer.setOutput(writer)
            xmlSerializer.startDocument("UTF-8", true)
            xmlSerializer.startTag("", "Sessions")

            for (session in sessions) {
                xmlSerializer.startTag("", "Session")
                xmlSerializer.attribute("", "id", session.id)

                xmlSerializer.startTag("", "Title")
                xmlSerializer.text(session.title)
                xmlSerializer.endTag("", "Title")

                xmlSerializer.startTag("", "Description")
                xmlSerializer.text(session.description)
                xmlSerializer.endTag("", "Description")

                xmlSerializer.startTag("", "ClientName")
                xmlSerializer.text(session.clientName)
                xmlSerializer.endTag("", "ClientName")

                xmlSerializer.startTag("", "CourtDate")
                xmlSerializer.text(session.courtDate)
                xmlSerializer.endTag("", "CourtDate")

                xmlSerializer.startTag("", "CourtTime")
                xmlSerializer.text(session.courtTime)
                xmlSerializer.endTag("", "CourtTime")

                xmlSerializer.startTag("", "CourtBranch")
                xmlSerializer.text(session.courtBranch)
                xmlSerializer.endTag("", "CourtBranch")

                xmlSerializer.startTag("", "Status")
                xmlSerializer.text(session.status)
                xmlSerializer.endTag("", "Status")

                xmlSerializer.startTag("", "AddedDate")
                xmlSerializer.text(session.addedDate)
                xmlSerializer.endTag("", "AddedDate")

                xmlSerializer.endTag("", "Session")
            }
            xmlSerializer.endTag("", "Sessions")
            xmlSerializer.endDocument()
            fileOutputStream.write(writer.toString().toByteArray())
        }
    }

    fun loadSessions(context: Context): List<CourtSession> {
        createEmptyXmlFile(context, SESSIONS_FILE_NAME, "Sessions")
        val sessions = mutableListOf<CourtSession>()
        val file = File(context.filesDir, SESSIONS_FILE_NAME)
        if (!file.exists() || file.length() == 0L) {
            return sessions
        }

        FileInputStream(file).use { fileInputStream ->
            val parserFactory = XmlPullParserFactory.newInstance()
            val parser = parserFactory.newPullParser()
            parser.setInput(fileInputStream, null)

            var eventType = parser.eventType
            var currentSession: CourtSession? = null
            var text: String? = ""

            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = parser.name
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if (tagName == "Session") {
                            currentSession = CourtSession(
                                id = parser.getAttributeValue("", "id") ?: UUID.randomUUID().toString(),
                                title = "", description = "", clientName = "",
                                courtDate = "", courtTime = "", courtBranch = "",
                                status = "", addedDate = ""
                            )
                        }
                    }
                    XmlPullParser.TEXT -> text = parser.text
                    XmlPullParser.END_TAG -> {
                        when (tagName) {
                            "Session" -> currentSession?.let { sessions.add(it) }
                            "Title" -> currentSession = currentSession?.copy(title = text ?: "")
                            "Description" -> currentSession = currentSession?.copy(description = text ?: "")
                            "ClientName" -> currentSession = currentSession?.copy(clientName = text ?: "")
                            "CourtDate" -> currentSession = currentSession?.copy(courtDate = text ?: "")
                            "CourtTime" -> currentSession = currentSession?.copy(courtTime = text ?: "")
                            "CourtBranch" -> currentSession = currentSession?.copy(courtBranch = text ?: "")
                            "Status" -> currentSession = currentSession?.copy(status = text ?: "")
                            "AddedDate" -> currentSession = currentSession?.copy(addedDate = text ?: "")
                        }
                    }
                }
                eventType = parser.next()
            }
        }
        return sessions
    }

    // --- Meetings ---
    fun saveMeetings(context: Context, meetings: List<Meeting>) {
        createEmptyXmlFile(context, MEETINGS_FILE_NAME, "Meetings")
        val file = File(context.filesDir, MEETINGS_FILE_NAME)
        FileOutputStream(file).use { fileOutputStream ->
            val xmlSerializer = XmlPullParserFactory.newInstance().newSerializer()
            val writer = StringWriter()

            xmlSerializer.setOutput(writer)
            xmlSerializer.startDocument("UTF-8", true)
            xmlSerializer.startTag("", "Meetings")

            for (meeting in meetings) {
                xmlSerializer.startTag("", "Meeting")
                xmlSerializer.attribute("", "id", meeting.id)

                xmlSerializer.startTag("", "Title")
                xmlSerializer.text(meeting.title)
                xmlSerializer.endTag("", "Title")

                xmlSerializer.startTag("", "ClientName")
                xmlSerializer.text(meeting.clientName)
                xmlSerializer.endTag("", "ClientName")

                xmlSerializer.startTag("", "Description")
                xmlSerializer.text(meeting.description)
                xmlSerializer.endTag("", "Description")

                xmlSerializer.startTag("", "Date")
                xmlSerializer.text(meeting.date)
                xmlSerializer.endTag("", "Date")

                xmlSerializer.startTag("", "Time")
                xmlSerializer.text(meeting.time)
                xmlSerializer.endTag("", "Time")

                xmlSerializer.startTag("", "AddedDate")
                xmlSerializer.text(meeting.addedDate)
                xmlSerializer.endTag("", "AddedDate")

                xmlSerializer.endTag("", "Meeting")
            }
            xmlSerializer.endTag("", "Meetings")
            xmlSerializer.endDocument()
            fileOutputStream.write(writer.toString().toByteArray())
        }
    }

    fun loadMeetings(context: Context): List<Meeting> {
        createEmptyXmlFile(context, MEETINGS_FILE_NAME, "Meetings")
        val meetings = mutableListOf<Meeting>()
        val file = File(context.filesDir, MEETINGS_FILE_NAME)
        if (!file.exists() || file.length() == 0L) {
            return meetings
        }

        FileInputStream(file).use { fileInputStream ->
            val parserFactory = XmlPullParserFactory.newInstance()
            val parser = parserFactory.newPullParser()
            parser.setInput(fileInputStream, null)

            var eventType = parser.eventType
            var currentMeeting: Meeting? = null
            var text: String? = ""

            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = parser.name
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if (tagName == "Meeting") {
                            currentMeeting = Meeting(
                                id = parser.getAttributeValue("", "id") ?: UUID.randomUUID().toString(),
                                title = "", clientName = "", description = "",
                                date = "", time = "", addedDate = ""
                            )
                        }
                    }
                    XmlPullParser.TEXT -> text = parser.text
                    XmlPullParser.END_TAG -> {
                        when (tagName) {
                            "Meeting" -> currentMeeting?.let { meetings.add(it) }
                            "Title" -> currentMeeting = currentMeeting?.copy(title = text ?: "")
                            "ClientName" -> currentMeeting = currentMeeting?.copy(clientName = text ?: "")
                            "Description" -> currentMeeting = currentMeeting?.copy(description = text ?: "")
                            "Date" -> currentMeeting = currentMeeting?.copy(date = text ?: "")
                            "Time" -> currentMeeting = currentMeeting?.copy(time = text ?: "")
                            "AddedDate" -> currentMeeting = currentMeeting?.copy(addedDate = text ?: "")
                        }
                    }
                }
                eventType = parser.next()
            }
        }
        return meetings
    }

    // --- Documents ---
    private const val DOCUMENTS_XML_FILE = "documents.xml"

    fun saveDocuments(context: Context, documents: List<Document>) {
        createEmptyXmlFile(context, DOCUMENTS_XML_FILE, "Documents")
        val file = File(context.filesDir, DOCUMENTS_XML_FILE)
        FileOutputStream(file).use { fileOutputStream ->
            val xmlSerializer = XmlPullParserFactory.newInstance().newSerializer()
            val writer = StringWriter()

            xmlSerializer.setOutput(writer)
            xmlSerializer.startDocument("UTF-8", true)
            xmlSerializer.startTag("", "Documents")

            for (document in documents) {
                xmlSerializer.startTag("", "Document")
                xmlSerializer.attribute("", "id", document.id)
                xmlSerializer.attribute("", "relatedCaseId", document.relatedCaseId)

                xmlSerializer.startTag("", "Name")
                xmlSerializer.text(document.name)
                xmlSerializer.endTag("", "Name")

                xmlSerializer.startTag("", "FilePath")
                xmlSerializer.text(document.filePath)
                xmlSerializer.endTag("", "FilePath")

                xmlSerializer.startTag("", "MimeType")
                xmlSerializer.text(document.mimeType)
                xmlSerializer.endTag("", "MimeType")

                xmlSerializer.endTag("", "Document")
            }
            xmlSerializer.endTag("", "Documents")
            xmlSerializer.endDocument()
            fileOutputStream.write(writer.toString().toByteArray())
        }
    }

    fun loadDocuments(context: Context): List<Document> {
        createEmptyXmlFile(context, DOCUMENTS_XML_FILE, "Documents")
        val documents = mutableListOf<Document>()
        val file = File(context.filesDir, DOCUMENTS_XML_FILE)
        if (!file.exists() || file.length() == 0L) {
            return documents
        }

        FileInputStream(file).use { fileInputStream ->
            val parserFactory = XmlPullParserFactory.newInstance()
            val parser = parserFactory.newPullParser()
            parser.setInput(fileInputStream, null)

            var eventType = parser.eventType
            var currentDocument: Document? = null
            var text: String? = ""

            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = parser.name
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if (tagName == "Document") {
                            currentDocument = Document(
                                id = parser.getAttributeValue("", "id") ?: UUID.randomUUID().toString(),
                                name = "", filePath = "", mimeType = "",
                                relatedCaseId = parser.getAttributeValue("", "relatedCaseId") ?: ""
                            )
                        }
                    }
                    XmlPullParser.TEXT -> text = parser.text
                    XmlPullParser.END_TAG -> {
                        when (tagName) {
                            "Document" -> currentDocument?.let { documents.add(it) }
                            "Name" -> currentDocument = currentDocument?.copy(name = text ?: "")
                            "FilePath" -> currentDocument = currentDocument?.copy(filePath = text ?: "")
                            "MimeType" -> currentDocument = currentDocument?.copy(mimeType = text ?: "")
                        }
                    }
                }
                eventType = parser.next()
            }
        }
        return documents
    }

    fun deleteDocument(context: Context, documentId: String) {
        val documents = loadDocuments(context).toMutableList()
        val initialSize = documents.size
        documents.removeIf { it.id == documentId }
        if (documents.size < initialSize) {
            saveDocuments(context, documents)
        }
    }

    fun deleteDocumentsForCase(context: Context, caseId: String) {
        val documents = loadDocuments(context).toMutableList()
        val initialSize = documents.size
        documents.removeIf { it.relatedCaseId == caseId }
        if (documents.size < initialSize) {
            saveDocuments(context, documents)
        }
    }

    // --- Daily Tasks ---
    private const val DAILY_TASKS_FILE = "daily_tasks.xml"

    fun saveDailyTasks(context: Context, tasks: List<DailyTask>) {
        createEmptyXmlFile(context, DAILY_TASKS_FILE, "DailyTasks")
        val file = File(context.filesDir, DAILY_TASKS_FILE)
        FileOutputStream(file).use { fileOutputStream ->
            val xmlSerializer = XmlPullParserFactory.newInstance().newSerializer()
            val writer = StringWriter()

            xmlSerializer.setOutput(writer)
            xmlSerializer.startDocument("UTF-8", true)
            xmlSerializer.startTag("", "DailyTasks")

            for (task in tasks) {
                xmlSerializer.startTag("", "DailyTask")
                xmlSerializer.attribute("", "id", task.id)

                xmlSerializer.startTag("", "Title")
                xmlSerializer.text(task.title)
                xmlSerializer.endTag("", "Title")

                xmlSerializer.startTag("", "Description")
                xmlSerializer.text(task.description)
                xmlSerializer.endTag("", "Description")

                xmlSerializer.startTag("", "DueDate")
                xmlSerializer.text(task.dueDate)
                xmlSerializer.endTag("", "DueDate")

                // Save relatedClientOrCase if it exists
                task.relatedClientOrCase?.let {
                    xmlSerializer.startTag("", "RelatedClientOrCase")
                    xmlSerializer.text(it)
                    xmlSerializer.endTag("", "RelatedClientOrCase")
                }

                xmlSerializer.endTag("", "DailyTask")
            }
            xmlSerializer.endTag("", "DailyTasks")
            xmlSerializer.endDocument()
            fileOutputStream.write(writer.toString().toByteArray())
        }
    }

    fun loadDailyTasks(context: Context): List<DailyTask> {
        createEmptyXmlFile(context, DAILY_TASKS_FILE, "DailyTasks")
        val tasks = mutableListOf<DailyTask>()
        val file = File(context.filesDir, DAILY_TASKS_FILE)
        if (!file.exists() || file.length() == 0L) {
            return tasks
        }

        FileInputStream(file).use { fileInputStream ->
            val parserFactory = XmlPullParserFactory.newInstance()
            val parser = parserFactory.newPullParser()
            parser.setInput(fileInputStream, null)

            var eventType = parser.eventType
            var currentTask: DailyTask? = null
            var text: String? = ""

            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = parser.name
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if (tagName == "DailyTask") {
                            currentTask = DailyTask(
                                id = parser.getAttributeValue("", "id") ?: UUID.randomUUID().toString(),
                                title = "", description = "", dueDate = "", relatedClientOrCase = null
                            )
                        }
                    }
                    XmlPullParser.TEXT -> text = parser.text
                    XmlPullParser.END_TAG -> {
                        when (tagName) {
                            "DailyTask" -> currentTask?.let { tasks.add(it) }
                            "Title" -> currentTask = currentTask?.copy(title = text ?: "")
                            "Description" -> currentTask = currentTask?.copy(description = text ?: "")
                            "DueDate" -> currentTask = currentTask?.copy(dueDate = text ?: "")
                            "RelatedClientOrCase" -> currentTask = currentTask?.copy(relatedClientOrCase = text ?: "")
                        }
                    }
                }
                eventType = parser.next()
            }
        }
        return tasks
    }
}
