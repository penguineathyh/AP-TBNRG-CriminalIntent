package com.example.criminalintent.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.criminalintent.R
import com.example.criminalintent.add
import com.example.criminalintent.ui.CrimeFragment.Companion.TAG_CRIME_FRAGMENT
import com.example.criminalintent.ui.CrimeListFragment.Companion.TAG_CRIME_LIST_FRAGMENT

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.add(
            R.id.fragment_container,
            CrimeListFragment.newInstance(),
            TAG_CRIME_LIST_FRAGMENT
        )
    }
}
