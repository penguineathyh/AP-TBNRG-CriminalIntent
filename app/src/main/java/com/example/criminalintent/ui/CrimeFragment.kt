package com.example.criminalintent.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.criminalintent.CrimeDetailViewModel
import com.example.criminalintent.DateUtil
import com.example.criminalintent.R
import com.example.criminalintent.model.Crime
import kotlinx.android.synthetic.main.fragment_crime.view.button_crime_date
import kotlinx.android.synthetic.main.fragment_crime.view.checkbox_crime_solved
import kotlinx.android.synthetic.main.fragment_crime.view.edit_text_crime_title
import java.util.Date
import java.util.UUID

class CrimeFragment : Fragment(), DatePickerFragment.Callbacks {

    private lateinit var crime: Crime

    private val viewModel: CrimeDetailViewModel by lazy {
        ViewModelProvider(this).get(CrimeDetailViewModel::class.java)
    }

    private lateinit var crimeTitleEditText: EditText
    private lateinit var crimeDateButton: Button
    private lateinit var crimeSolvedCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val crimeId = requireArguments().getSerializable(ARG_CRIME_ID) as UUID
        viewModel.loadCrime(crimeId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime, container, false)

        crimeTitleEditText = view.edit_text_crime_title
        crimeDateButton = view.button_crime_date
        crimeSolvedCheckBox = view.checkbox_crime_solved

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.crimeLiveData.observe(
            viewLifecycleOwner,
            Observer { crime ->
                crime?.let {
                    this.crime = crime
                    updateUI()
                }
            }
        )
    }

    override fun onStart() {
        super.onStart()

        val titleWatcher = object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                crime = crime.copy(title = s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }
        }

        crimeTitleEditText.addTextChangedListener(titleWatcher)

        crimeSolvedCheckBox.setOnCheckedChangeListener { _, isChecked ->
            crime = crime.copy(isSolved = isChecked)
        }

        crimeDateButton.setOnClickListener {
            showDatePickerFragmentForResult()
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.saveCrime(crime)
    }

    private fun updateUI() {
        crimeTitleEditText.setText(crime.title)
        crimeDateButton.text = DateUtil.format(crime.date)
        crimeSolvedCheckBox.let {
            it.isChecked = crime.isSolved
            it.jumpDrawablesToCurrentState()
        }
    }

    private fun showDatePickerFragmentForResult() {
        val fragment =
            DatePickerFragment.showDialogWithGivenDate(requireFragmentManager(), crime.date)
        fragment.setTargetFragment(this, REQUEST_CODE_DATE)
    }

    override fun onDateSelected(date: Date) {
        crime = crime.copy(date = date)
        updateUI()
    }

    companion object {

        private const val TAG = "crime.fragment"
        private const val ARG_CRIME_ID = "crime.id"
        private const val REQUEST_CODE_DATE = 0

        fun newInstance(crimeId: UUID): CrimeFragment {
            Log.d(TAG, "CrimeFragment Instance: crimeId = $crimeId")
            val args = Bundle().also {
                it.putSerializable(ARG_CRIME_ID, crimeId)
            }
            return CrimeFragment().also { it.arguments = args }
        }
    }

}