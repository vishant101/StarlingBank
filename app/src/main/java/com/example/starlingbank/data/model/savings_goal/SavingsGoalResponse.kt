package com.example.starlingbank.data.model.savings_goal

import androidx.room.Entity

@Entity
data class SavingsGoalResponse (
    val savingsGoalUid: String,
    val success: Boolean,
    val errors: List<String>
)