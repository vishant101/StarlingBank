package com.example.starlingbank.data.model

import androidx.room.Entity

@Entity
data class Error (
    val message: String
)