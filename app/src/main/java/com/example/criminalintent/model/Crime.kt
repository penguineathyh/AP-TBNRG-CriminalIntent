package com.example.criminalintent.model

import com.example.criminalintent.DateUtil
import java.util.UUID

data class Crime(
    val id: UUID = UUID.randomUUID(),
    val title: String = "",
    val date: String = DateUtil.currentDate(),
    val isSolved: Boolean = false
)