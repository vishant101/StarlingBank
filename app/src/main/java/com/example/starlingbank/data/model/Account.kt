package com.example.starlingbank.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Account (
    @PrimaryKey
    val accountUid: String,
    val defaultCategory: String,
    val currency: String,
    val createdAt: String
)