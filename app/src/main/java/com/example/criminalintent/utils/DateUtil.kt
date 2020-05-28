package com.example.criminalintent.utils

import android.content.Context
import android.os.Build
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtil {

    // for example, "Monday, Jul 22, 2019."
    private const val PATTERN = "EEEE, MMM d, yyyy"

    private lateinit var locale: Locale

    private val df: DateFormat by lazy {
        val sdf = DateFormat.getDateInstance(DateFormat.FULL, locale)
        try {
            sdf as SimpleDateFormat
            sdf.applyPattern(PATTERN)
        } catch (e: Exception) {
            //
        }
        sdf
    }

    fun initialize(context: Context) {
        locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            context.resources.configuration.locale
        }
    }

    fun format(date: Date) = df.format(date)
}