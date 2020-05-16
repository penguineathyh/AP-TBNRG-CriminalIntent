package com.example.criminalintent.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.criminalintent.DateUtil
import java.util.UUID

@Entity
data class Crime(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val title: String = "",
    val date: String = DateUtil.currentDate(),
    val isSolved: Boolean = false
)