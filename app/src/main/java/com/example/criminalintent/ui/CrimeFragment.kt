package com.example.criminalintent.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.criminalintent.R
import com.example.criminalintent.model.Crime
import kotlinx.android.synthetic.main.fragment_crime.view.*

class CrimeFragment:Fragment() {

    private lateinit var crime:Crime

    private lateinit var crimeTitleEditText:EditText
    private lateinit var crimeDateButton:Button
    private lateinit var crimeSolvedCheckBox:CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crime = Crime()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime, container, false)

        crimeTitleEditText = view.edit_text_crime_title
        crimeDateButton = view.button_crime_date.also {
            it.text = crime.date.toString()
            it.isEnabled = false
        }
        crimeSolvedCheckBox = view.checkbox_crime_solved

        return view
    }

    override fun onStart() {
        super.onStart()

        val titleWatcher = object :TextWatcher{

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
    }

    companion object{
        const val TAG_CRIME_FRAGMENT = "crime.fragment"
    }

}