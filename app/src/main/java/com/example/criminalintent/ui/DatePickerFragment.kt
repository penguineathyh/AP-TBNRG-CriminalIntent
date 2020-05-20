package com.example.criminalintent.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import java.util.Calendar
import java.util.Date

class DatePickerFragment private constructor() : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val date = requireArguments().getSerializable(ARG_CRIME_DATE) as Date
        val calendar = Calendar.getInstance().also {
            it.time = date
        }
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(
            requireContext(),
            null,
            initialYear,
            initialMonth,
            initialDay
        )
    }

    companion object {

        private const val TAG = "date.picker.dialog"
        private const val ARG_CRIME_DATE = "crime.date"

        fun showDialogWithGivenDate(fragmentManager: FragmentManager, date: Date) {
            val fragment = DatePickerFragment()
            val args = Bundle().also {
                it.putSerializable(ARG_CRIME_DATE, date)
                fragment.arguments = it
            }
            fragment.show(fragmentManager, TAG)
        }
    }
}