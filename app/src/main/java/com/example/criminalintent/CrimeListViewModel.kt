package com.example.criminalintent

import androidx.lifecycle.ViewModel
import com.example.criminalintent.model.Crime

class CrimeListViewModel : ViewModel() {

    val crimes: MutableList<Crime> by lazy {
        val list = mutableListOf<Crime>()
        for (i in 0 until 100) {
            list += Crime(title = "Crime #$i", isSolved = i % 2 == 0)
        }
        list
    }
}