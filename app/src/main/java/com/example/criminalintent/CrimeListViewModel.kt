package com.example.criminalintent

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.criminalintent.database.CrimeRepository
import com.example.criminalintent.model.Crime

class CrimeListViewModel : ViewModel() {

    val crimeListLiveData = CrimeRepository.instance.getCrimes()

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "crimeListViewModel:$this call onCleared()")

    }

    companion object{
        private const val TAG = "crime.list.viewmodel"
    }
}