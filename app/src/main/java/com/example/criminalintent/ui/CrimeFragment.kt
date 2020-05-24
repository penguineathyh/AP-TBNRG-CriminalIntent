package com.example.criminalintent.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.criminalintent.CrimeDetailViewModel
import com.example.criminalintent.DateUtil
import com.example.criminalintent.R
import com.example.criminalintent.StringGetter
import com.example.criminalintent.model.Crime
import kotlinx.android.synthetic.main.fragment_crime.view.button_choose_suspect
import kotlinx.android.synthetic.main.fragment_crime.view.button_crime_date
import kotlinx.android.synthetic.main.fragment_crime.view.button_open_camera
import kotlinx.android.synthetic.main.fragment_crime.view.button_send_crime_report
import kotlinx.android.synthetic.main.fragment_crime.view.checkbox_crime_solved
import kotlinx.android.synthetic.main.fragment_crime.view.edit_text_crime_title
import kotlinx.android.synthetic.main.fragment_crime.view.image_view_crime_photo
import java.util.Date
import java.util.UUID

class CrimeFragment : Fragment(), DatePickerFragment.Callbacks {

    private lateinit var crime: Crime

    private val viewModel: CrimeDetailViewModel by lazy {
        ViewModelProvider(this).get(CrimeDetailViewModel::class.java)
    }

    private lateinit var crimeImageView:ImageView
    private lateinit var cameraButton:ImageButton
    private lateinit var crimeTitleEditText: EditText
    private lateinit var crimeDateButton: Button
    private lateinit var crimeSolvedCheckBox: CheckBox
    private lateinit var chooseSuspectButton: Button

    private val pickContactIntent: Intent by lazy {
        Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
    }

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

        crimeImageView = view.image_view_crime_photo
        cameraButton = view.button_open_camera

        crimeTitleEditText = view.edit_text_crime_title
        crimeDateButton = view.button_crime_date
        crimeSolvedCheckBox = view.checkbox_crime_solved
        chooseSuspectButton = view.button_choose_suspect

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.crimeLiveData.observe(
            viewLifecycleOwner,
            Observer { crime ->
                Log.d(TAG, "update crime:$crime")
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

        chooseSuspectButton.setOnClickListener {
            startActivityForResult(pickContactIntent, REQUEST_CODE_CONTACT)
        }

        checkContactsAppExistence()

        requireView().button_send_crime_report.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, crime.generateReport())
                putExtra(
                    Intent.EXTRA_SUBJECT,
                    StringGetter.getString(R.string.crime_report_subject)
                )
            }.also { intent ->
                startActivity(
                    Intent.createChooser(
                        intent,
                        StringGetter.getString(R.string.text_send_crime_report)
                    )
                )
            }
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.saveCrime(crime)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when {
            resultCode != Activity.RESULT_OK || requestCode != REQUEST_CODE_CONTACT || data?.data == null -> {
                super.onActivityResult(requestCode, resultCode, data)
                return
            }
            else -> {
                val contactUri: Uri = data.data!!
                val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
                val cursor = requireActivity().contentResolver.query(
                    contactUri,
                    queryFields,
                    null,
                    null,
                    null
                )
                cursor?.use {
                    if (it.count == 0) {
                        super.onActivityResult(requestCode, resultCode, data)
                        return
                    }
                    it.moveToFirst()
                    crime = crime.copy(suspect = it.getString(0))
                    viewModel.saveCrime(crime)
                }
            }
        }
    }

    private fun updateUI() {
        crimeTitleEditText.setText(crime.title)
        crimeDateButton.text = DateUtil.format(crime.date)
        crimeSolvedCheckBox.let {
            it.isChecked = crime.isSolved
            it.jumpDrawablesToCurrentState()
        }
        chooseSuspectButton.text =
            if (crime.hasSuspect) crime.suspect else getString(R.string.button_choose_suspect)

    }

    private fun showDatePickerFragmentForResult() {
        val fragment =
            DatePickerFragment.showDialogWithGivenDate(requireFragmentManager(), crime.date)
        fragment.setTargetFragment(this, REQUEST_CODE_DATE)
    }

    private fun checkContactsAppExistence() {
        val packageManager = requireActivity().packageManager
        val result =
            packageManager.resolveActivity(pickContactIntent, PackageManager.MATCH_DEFAULT_ONLY)
        result ?: run { chooseSuspectButton.isEnabled = false }
    }

    override fun onDateSelected(date: Date) {
        crime = crime.copy(date = date)
        updateUI()
    }

    companion object {

        private const val TAG = "crime.fragment"
        private const val ARG_CRIME_ID = "crime.id"
        private const val REQUEST_CODE_DATE = 0
        private const val REQUEST_CODE_CONTACT = 1

        fun newInstance(crimeId: UUID): CrimeFragment {
            Log.d(TAG, "CrimeFragment Instance: crimeId = $crimeId")
            val args = Bundle().also {
                it.putSerializable(ARG_CRIME_ID, crimeId)
            }
            return CrimeFragment().also { it.arguments = args }
        }
    }

}