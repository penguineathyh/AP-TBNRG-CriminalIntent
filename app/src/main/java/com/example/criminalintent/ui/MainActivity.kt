package com.example.criminalintent.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.criminalintent.R
import com.example.criminalintent.add
import com.example.criminalintent.replace
import java.util.UUID

class MainActivity : AppCompatActivity(), CrimeListFragment.Callbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCrimeSelected(crimeId: UUID) {
        Log.d(TAG, "MainActivity.onCrimeSelected: $crimeId")
        val crimeFragment = CrimeFragment.newInstance(crimeId)
        supportFragmentManager.replace(R.id.fragment_container, crimeFragment)
    }

    companion object {
        private const val TAG = "main.activity"
    }
}
