package com.example.criminalintent.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels
import com.example.criminalintent.CrimeDetailViewModel
import com.example.criminalintent.R
import com.example.criminalintent.model.Crime
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

class DatePickerFragment : DialogFragment() {

    private lateinit var crime: Crime

    private val viewModel: CrimeDetailViewModel by navGraphViewModels(R.id.crime_navigation_graph)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

        val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val resultDate: Date = GregorianCalendar(year, month, dayOfMonth).time
            crime = crime.copy(date = resultDate)
        }
        return DatePickerDialog(
            requireContext(),
            dateListener,
            initialYear,
            initialMonth,
            initialDay
        )
    }

    override fun onStart() {
        super.onStart()
        viewModel.crimeLiveData.observe(
            this,
            Observer { crime ->
                crime?.let {
                    this.crime = it
                    updateDialog()
                }
            }
        )
    }

    override fun onStop() {
        super.onStop()
        viewModel.saveCrime(crime)
    }

    private fun updateDialog() {
        val dialog = requireDialog() as DatePickerDialog
        val calendar = Calendar.getInstance().also { it.time = crime.date }
        dialog.updateDate(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }
}