package com.example.starlingbank.data.model

import androidx.room.Entity

@Entity
data class Accounts (
    val accounts: List<Account>
)