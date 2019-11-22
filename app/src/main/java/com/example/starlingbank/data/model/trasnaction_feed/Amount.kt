package com.example.starlingbank.data.model.trasnaction_feed

import androidx.room.Entity

@Entity
data class Amount (
    val currency: String,
    val minorUnits: Int
)