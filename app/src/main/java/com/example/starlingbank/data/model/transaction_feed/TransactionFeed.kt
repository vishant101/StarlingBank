package com.example.starlingbank.data.model.transaction_feed

import androidx.room.Entity

@Entity
data class TransactionFeed (
    val feedItems: List<FeedItem>
)