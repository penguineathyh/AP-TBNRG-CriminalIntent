package com.example.criminalintent.database

import android.content.Context
import androidx.room.Room
import com.example.criminalintent.model.Crime
import java.util.UUID

class CrimeRepository private constructor() {

    private lateinit var database: CrimeDatabase
    private val crimeDao: CrimeDao by lazy {
        database.crimeDao()
    }

    fun initialize(context: Context) {
        database = Room.databaseBuilder(
            context,
            CrimeDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    fun getCrimes(): List<Crime> = crimeDao.getCrimes()

    fun getCrime(id: UUID): Crime? = crimeDao.getCrime(id)

    private object CrimeRepositoryHolder {
        val crimeRepository = CrimeRepository()
    }

    companion object {
        val instance = CrimeRepositoryHolder.crimeRepository

        private const val DATABASE_NAME = "crime-database"
    }
}