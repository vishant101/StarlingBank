package com.example.starlingbank.data.model.accounts

import androidx.room.Entity

@Entity
data class Accounts (
    val accounts: List<Account>
)