package com.example.criminalintent.database

import androidx.room.TypeConverter
import java.util.UUID

class CrimeTypeConverters {

    @TypeConverter
    fun toUUID(uuid: String?): UUID? = UUID.fromString(uuid)

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? = uuid?.toString()
}