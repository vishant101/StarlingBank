package com.example.starlingbank.data.model.savings_goal

import androidx.room.Entity
import com.example.starlingbank.data.model.Error

@Entity
data class SavingsGoalResponse (
    val savingsGoalUid: String,
    val success: Boolean,
    val errors: List<Error>
)