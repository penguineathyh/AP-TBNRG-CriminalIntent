package com.example.criminalintent

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

object DateUtil {

    // for example, "Monday, Jul 22, 2019."
    private const val PATTERN = "EEEE, MMM d, yyyy"

    private val df: DateFormat by lazy {
        val sdf = DateFormat.getDateInstance()
        try {
            sdf as SimpleDateFormat
            sdf.applyPattern(PATTERN)
        } catch (e: Exception) {
            //
        }
        sdf
    }

    fun format(date: Date) = df.format(date)
}