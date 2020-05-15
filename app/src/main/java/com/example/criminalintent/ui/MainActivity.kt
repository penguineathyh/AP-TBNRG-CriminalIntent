package com.example.criminalintent.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.criminalintent.R
import com.example.criminalintent.add
import com.example.criminalintent.ui.CrimeFragment.Companion.TAG_CRIME_FRAGMENT

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.add(
            R.id.fragment_container_crime_fragment,
            CrimeFragment(),
            TAG_CRIME_FRAGMENT
        )
    }
}
