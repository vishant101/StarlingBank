package com.example.starlingbank.data.model.trasnaction_feed

import androidx.room.Entity
import com.example.starlingbank.data.model.accounts.Account

@Entity
data class TransactionFeed (
    val feedItems: List<FeedItem>
)