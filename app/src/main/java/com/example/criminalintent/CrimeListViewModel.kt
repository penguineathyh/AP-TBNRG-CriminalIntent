package com.example.criminalintent

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.criminalintent.database.CrimeRepository
import com.example.criminalintent.model.Crime

class CrimeListViewModel : ViewModel() {

    val crimeListLiveData = CrimeRepository.instance.getCrimes()

    val noCrimeLiveData: LiveData<Boolean> =
        Transformations.switchMap(crimeListLiveData) {
            val result = MutableLiveData<Boolean>(false)
            result.value = it.isEmpty()
            result
        }

    fun addCrime(crime: Crime) {
        CrimeRepository.instance.addCrime(crime)
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "crimeListViewModel:$this call onCleared()")

    }

    companion object {
        private const val TAG = "crime.list.viewmodel"
    }
}