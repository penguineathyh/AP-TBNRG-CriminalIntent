package com.example.criminalintent

import android.app.Application
import com.example.criminalintent.database.CrimeRepository

class CriminalIntentApp : Application() {

    override fun onCreate() {
        super.onCreate()

        CrimeRepository.instance.initialize(this)
        StringGetter.initialize(this)

    }
}