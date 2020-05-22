package com.example.criminalintent.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.criminalintent.DateUtil
import com.example.criminalintent.R
import com.example.criminalintent.StringGetter
import java.util.Date
import java.util.UUID

@Entity
data class Crime(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val title: String = "",
    val date: Date = Date(),
    val isSolved: Boolean = false,
    val suspect: String = ""
) {

    fun generateReport(): String {
        val solvedString =
            StringGetter.getString(if (isSolved) R.string.text_report_solved else R.string.text_report_unsolved)
        val dateString = DateUtil.format(date)
        val suspect =
            if (!suspect.isBlank()) StringGetter.getStringWithArgs(
                R.string.text_report_suspect,
                suspect
            )
            else StringGetter.getString(R.string.text_report_no_suspect)

        return StringGetter.getStringWithArgs(
            R.string.crime_report,
            title,
            dateString,
            solvedString,
            suspect
        )
    }
}