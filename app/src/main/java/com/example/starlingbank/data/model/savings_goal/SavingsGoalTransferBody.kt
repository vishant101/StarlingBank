package com.example.starlingbank.data.model.savings_goal

import androidx.room.Entity
import com.example.starlingbank.data.model.transaction_feed.Amount

@Entity
data class SavingsGoalTransferBody (
    val amount: Amount
)