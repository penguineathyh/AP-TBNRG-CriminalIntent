package com.example.criminalintent.model

import java.util.*

data class Crime(
    val id: UUID = UUID.randomUUID(),
    val title: String = "",
    val date: Date = Date(),
    val isSolved: Boolean = false
)