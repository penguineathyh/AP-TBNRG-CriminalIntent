package com.example.criminalintent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.criminalintent.database.CrimeRepository
import com.example.criminalintent.model.Crime
import java.util.UUID

class CrimeDetailViewModel : ViewModel() {

    private val crimeRepository = CrimeRepository.instance
    private var crimeIdLiveData = MutableLiveData<UUID>()

    val crimeLiveData: LiveData<Crime?> =
        Transformations.switchMap(crimeIdLiveData) {
            crimeRepository.getCrime(it)
        }

    fun loadCrime(crimeId: UUID) {
        crimeIdLiveData.value = crimeId
    }

    fun saveCrime(crime: Crime) {
        crimeRepository.updateCrime(crime)
    }
}