package com.example.criminalintent.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.criminalintent.SingleThreadPoster
import com.example.criminalintent.model.Crime
import java.io.File
import java.util.UUID

class CrimeRepository private constructor() {

    private lateinit var database: CrimeDatabase
    private val crimeDao: CrimeDao by lazy {
        database.crimeDao()
    }

    private lateinit var filesDir: File

//    val MIGRATION_1_2 = object : Migration(1, 2) {
//        override fun migrate(database: SupportSQLiteDatabase) {
//            //
//        }
//    }

    fun initialize(context: Context) {
        database = Room.databaseBuilder(
            context,
            CrimeDatabase::class.java,
            DATABASE_NAME
        ).addMigrations(migration_1_2)
            .build()

        filesDir = context.applicationContext.filesDir
    }

    fun getCrimes(): LiveData<List<Crime>> = crimeDao.getCrimes()

    fun getCrime(id: UUID): LiveData<Crime?> = crimeDao.getCrime(id)

    fun updateCrime(crime: Crime) {
        SingleThreadPoster.post {
            crimeDao.updateCrime(crime)
        }
    }

    fun addCrime(crime: Crime) {
        SingleThreadPoster.post {
            crimeDao.addCrime(crime)
        }
    }

    fun getPhotoFile(crime: Crime) = File(filesDir, crime.photoFileName)

    private object CrimeRepositoryHolder {
        val crimeRepository = CrimeRepository()
    }

    companion object {
        val instance = CrimeRepositoryHolder.crimeRepository

        private const val DATABASE_NAME = "crime-database"
    }
}